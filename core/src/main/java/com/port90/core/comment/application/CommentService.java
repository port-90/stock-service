package com.port90.core.comment.application;

import com.port90.core.auth.domain.model.User;
import com.port90.core.auth.infrastructure.UserRepository;
import com.port90.core.comment.aop.Retry;
import com.port90.core.comment.domain.exception.CommentException;
import com.port90.core.comment.domain.model.Comment;
import com.port90.core.comment.dto.ChildCommentCountDto;
import com.port90.core.comment.dto.CommentDto;
import com.port90.core.comment.dto.request.CommentCreateRequest;
import com.port90.core.comment.dto.request.CommentDeleteRequest;
import com.port90.core.comment.dto.request.CommentUpdateRequest;
import com.port90.core.comment.dto.response.CommentCreateResponse;
import com.port90.core.comment.dto.response.CommentUpdateResponse;
import com.port90.core.comment.infrastructure.CommentRepository;
import com.port90.stockdomain.domain.chart.StockChartMinute;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.port90.core.comment.domain.exception.CommentErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private static final String ANONYMOUS_AUTHOR = "익명";
    private final CommentRepository commentRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final StockChartManager stockChartManager;

    @Transactional
    public CommentCreateResponse createComment(Long userId, CommentCreateRequest request) {

        StockChartMinute stockChartMinute = stockChartManager.getTheMostRecentStockChartMinuteByStockCode(request.stockCode());

        Comment comment = createComment(userId, request, stockChartMinute);

        validateParentId(request.parentId(), comment);

        Comment saved = commentRepository.save(comment);

        log.info("Comment Created, ID: {}", saved.getId());

        return CommentCreateResponse.from(saved);
    }

    public CommentUpdateResponse updateComment(Long userId, Long commentId, CommentUpdateRequest request) {
        Comment comment = commentRepository.getById(commentId);

        validateComment(userId, request.password(), comment);

        comment.updateContent(request.content());

        Comment saved = commentRepository.save(comment);

        log.info("Comment Updated, ID: {}", saved.getId());

        return CommentUpdateResponse.from(saved);
    }

    @Transactional
    public void deleteComment(Long userId, Long commentId, CommentDeleteRequest request) {
        Comment comment = commentRepository.getById(commentId);

        validateComment(userId, request.password(), comment);

        if (comment.isParent()) {
            deleteChildComment(comment);
        }
        if (comment.isChild()) {
            updateParentComment(comment);
        }

        commentRepository.delete(comment);

        log.info("Comment Deleted, ID: {}", comment.getId());
    }

    public List<CommentDto> getParentsByStockCode(String stockCode, Long cursor, int size) {
        List<Comment> comments = commentRepository.findParentsByStockCodeByCursor(stockCode, cursor, size);

        return getCommentDtos(comments);
    }

    public List<CommentDto> getParentsByStockChartMinute(String stockCode, LocalDate date, LocalTime time, Long cursor, int size) {
        List<Comment> comments = commentRepository.findParentsByStockCodeAndDateAndTimeByCursor(stockCode, date, time, cursor, size);

        return getCommentDtos(comments);
    }

    public List<CommentDto> getParentsByStockChartHourly(String stockCode, LocalDate date, LocalTime time, Long cursor, int size) {
        LocalTime startTime = time.minusHours(1);
        List<Comment> comments = commentRepository.findParentsByStockCodeAndDateAndTimeBetweenByCursor(stockCode, date, startTime, time, cursor, size);

        return getCommentDtos(comments);
    }

    public List<CommentDto> getParentsByStockChartDaily(String stockCode, LocalDate date, Long cursor, int size) {
        List<Comment> comments = commentRepository.findParentsByStockCodeAndDateByCursor(stockCode, date, cursor, size);

        return getCommentDtos(comments);
    }

    public List<CommentDto> getParentsByStockChartWeekly(String stockCode, LocalDate date, Long cursor, int size) {
        LocalDate startOfWeek = date.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = date.with(DayOfWeek.SUNDAY);
        List<Comment> comments = commentRepository.findParentsByStockCodeAndDateBetweenByCursor(stockCode, startOfWeek, endOfWeek, cursor, size);

        return getCommentDtos(comments);
    }

    public List<CommentDto> getParentsByStockChartMonthly(String stockCode, Integer year, Integer month, Long cursor, int size) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startOfMonth = yearMonth.atDay(1);
        LocalDate endOfMonth = yearMonth.atEndOfMonth();

        List<Comment> comments = commentRepository.findParentsByStockCodeAndDateBetweenByCursor(stockCode, startOfMonth, endOfMonth, cursor, size);

        return getCommentDtos(comments);
    }

    public List<CommentDto> getChildrenByParentId(Long parentId, Long cursor, int size) {
        List<Comment> comments = commentRepository.findChildrenByParentIdByCursor(parentId, cursor, size);

        return getCommentDtos(comments);
    }

    @Retry
    public void increaseLikeCount(Long commentId) {
        Comment comment = commentRepository.getByIdWithOptimisticLock(commentId);
        comment.increaseLikeCount();
        commentRepository.save(comment);
    }

    @Retry
    public void decreaseLikeCount(Long commentId) {
        Comment comment = commentRepository.getByIdWithOptimisticLock(commentId);
        comment.decreaseLikeCount();
        commentRepository.save(comment);
    }

    private Comment createComment(Long userId, CommentCreateRequest request, StockChartMinute stockChartMinute) {
        if (userId != null) {
            if (request.isAnonymous()) {
                log.info("create anonymous user comment");
                return Comment.createAnonymousUserComment(
                        stockChartMinute.getStockCode(), stockChartMinute.getDate(), stockChartMinute.getTime(),
                        userId, request.content(), request.parentId()
                );
            }

            log.info("create user comment");
            return Comment.createUserComment(
                    stockChartMinute.getStockCode(), stockChartMinute.getDate(), stockChartMinute.getTime(),
                    userId, request.content(), request.parentId()
            );
        }

        if (request.password() == null) {
            throw new CommentException(GUEST_PASSWORD_REQUIRED);
        }

        log.info("create guest comment");
        return Comment.createGuestComment(
                stockChartMinute.getStockCode(), stockChartMinute.getDate(), stockChartMinute.getTime(),
                request.content(), request.parentId(), passwordEncoder.encode(request.password())
        );
    }

    private void validateParentId(Long parentId, Comment comment) {
        if (parentId != null) {
            Comment parent = commentRepository.getById(parentId);
            if (parent.isChild()) {
                throw new CommentException(PARENT_COMMENT_IS_CHILD_COMMENT);
            }
            parent.hasChild();
            comment.hasParent();
            commentRepository.save(parent);
        }
    }

    private void validateComment(Long userId, String password, Comment comment) {
        if (comment.isGuestComment()) {
            if (password == null) {
                throw new CommentException(GUEST_PASSWORD_REQUIRED);
            }
            if (!passwordEncoder.matches(password, comment.getPassword())) {
                throw new CommentException(COMMENT_PASSWORD_UNMATCHED);
            }
            return;
        }

        if (userId == null) {
            throw new CommentException(AUTHENTICATION_REQUIRED);
        }
        if (comment.isNotWrittenBy(userId)) {
            throw new CommentException(COMMENT_USER_UNMATCHED);
        }
    }

    private void deleteChildComment(Comment comment) {
        List<Long> childIds = commentRepository.findIdsByParentId(comment.getId());
        int count = commentRepository.deleteAllByIdIn(childIds);
        log.info("{}번 댓글에 포함된 자식 댓글 {}개 삭제", comment.getId(), count);
    }

    private void updateParentComment(Comment comment) {
        long count = commentRepository.countByParentId(comment.getParentId());
        log.info("{}번 댓글에 포함된 자식 댓글 {}개", comment.getParentId(), count);
        if (count > 1) {
            return;
        }
        Comment parent = commentRepository.getById(comment.getParentId());
        parent.hasNotChild();
        commentRepository.save(parent);
    }

    private List<CommentDto> getCommentDtos(List<Comment> comments) {
        Map<Long, String> nameMap = getNameMap(comments);
        Map<Long, Long> commentCountMap = getCommentCountMap(comments);

        return comments.stream()
                .map(comment -> {
                    if (comment.isUserComment()) {
                        return CommentDto.from(
                                comment, nameMap.get(comment.getUserId()), commentCountMap.getOrDefault(comment.getId(), 0L)
                        );
                    }

                    return CommentDto.from(comment, ANONYMOUS_AUTHOR, commentCountMap.getOrDefault(comment.getId(), 0L));
                }).toList();
    }

    private Map<Long, String> getNameMap(List<Comment> comments) {
        List<Long> userIds = comments.stream()
                .filter(Comment::isUserComment)
                .map(Comment::getUserId)
                .toList();
        return userRepository.findAllByIdIn(userIds)
                .stream()
                .collect(Collectors.toMap(User::getId, User::getName));
    }

    private Map<Long, Long> getCommentCountMap(List<Comment> comments) {
        List<Long> commentIdList = comments.stream()
                .map(Comment::getId)
                .toList();
        return commentRepository.findChildCountsByParentIdIn(commentIdList)
                .stream()
                .collect(Collectors.toMap(
                        ChildCommentCountDto::commentId, ChildCommentCountDto::childCommentCount)
                );
    }
}
