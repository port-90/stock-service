package com.port90.core.comment.application;

import com.port90.core.auth.domain.model.User;
import com.port90.core.auth.infrastructure.UserRepository;
import com.port90.core.comment.aop.Retry;
import com.port90.core.comment.domain.exception.CommentException;
import com.port90.core.comment.domain.model.Comment;
import com.port90.core.comment.domain.model.GuestComment;
import com.port90.core.comment.domain.model.UserComment;
import com.port90.core.comment.dto.CommentDto;
import com.port90.core.comment.dto.request.*;
import com.port90.core.comment.dto.response.CommentUpdateResponse;
import com.port90.core.comment.dto.response.GuestCommentCreateResponse;
import com.port90.core.comment.dto.response.UserCommentCreateResponse;
import com.port90.core.comment.infrastructure.CommentRepository;
import com.port90.stockdomain.domain.chart.*;
import com.port90.stockdomain.infrastructure.StockInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.port90.core.comment.domain.exception.CommentErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final AuthorNameGenerator authorNameGenerator;
    private final PasswordEncoder passwordEncoder;
    private final StockInfoRepository stockInfoRepository;
    private final UserRepository userRepository;
    private final StockChartProvider stockChartProvider;

    @Transactional
    public UserCommentCreateResponse createUserComment(Long userId, UserCommentCreateRequest request) {

        validateStockCode(request.stockCode());

        UserComment userComment = create(userId, request);

        validateParentId(request.parentId(), userComment);

        Comment comment = commentRepository.save(userComment);

        log.info("User Comment Created, ID: {}", comment.getId());

        return UserCommentCreateResponse.from(comment);
    }

    @Transactional
    public GuestCommentCreateResponse createGuestComment(GuestCommentCreateRequest request) {

        validateStockCode(request.stockCode());

        GuestComment guestComment = create(request);

        validateParentId(request.parentId(), guestComment);

        Comment comment = commentRepository.save(guestComment);

        log.info("Guest Comment Created, ID: {}", comment.getId());

        return GuestCommentCreateResponse.from(comment);
    }

    @Transactional
    public CommentUpdateResponse updateUserComment(Long userId, Long commentId, UserCommentUpdateRequest request) {
        Comment comment = commentRepository.findById(commentId);

        UserComment userComment = (UserComment) comment;
        if (userComment.isNotWrittenBy(userId)) {
            throw new CommentException(COMMENT_USER_UNMATCHED);
        }

        userComment.updateContent(request.content());

        commentRepository.save(userComment);

        log.info("User Comment Updated, ID: {}", userComment.getId());

        return CommentUpdateResponse.from(userComment);
    }

    @Transactional
    public CommentUpdateResponse updateGuestComment(Long commentId, GuestCommentUpdateRequest request) {
        Comment comment = commentRepository.findById(commentId);

        GuestComment guestComment = (GuestComment) comment;
        if (passwordUnmatched(request.password(), guestComment)) {
            throw new CommentException(GUEST_PASSWORD_UNMATCHED);
        }

        guestComment.updateContent(request.content());

        commentRepository.save(guestComment);

        log.info("Guest Comment Updated, ID: {}", guestComment.getId());

        return CommentUpdateResponse.from(guestComment);
    }

    @Transactional
    public void deleteUserComment(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId);

        UserComment userComment = (UserComment) comment;
        if (userComment.isNotWrittenBy(userId)) {
            throw new CommentException(COMMENT_USER_UNMATCHED);
        }

        if (userComment.isParent()) {
            deleteChildComment(userComment);
        }
        if (userComment.isChild()) {
            updateParentComment(userComment);
        }

        commentRepository.delete(userComment);

        log.info("User Comment Deleted, ID: {}", userComment.getId());
    }

    @Transactional
    public void deleteGuestComment(Long commentId, GuestCommentDeleteRequest request) {
        Comment comment = commentRepository.findById(commentId);

        GuestComment guestComment = (GuestComment) comment;
        if (passwordUnmatched(request.password(), guestComment)) {
            throw new CommentException(GUEST_PASSWORD_UNMATCHED);
        }

        if (guestComment.isParent()) {
            deleteChildComment(guestComment);
        }
        if (guestComment.isChild()) {
            updateParentComment(guestComment);
        }

        commentRepository.delete(guestComment);

        log.info("Guest Comment Deleted, ID: {}", guestComment.getId());
    }

    public List<CommentDto> getCommentList(String stockCode, Long cursor, int size) {
        List<Comment> comments = commentRepository.findCommentsByStockCodeByCursor(stockCode, cursor, size);

        return getCommentDtos(comments);
    }

    public List<CommentDto> getCommentListByMinute(String stockCode, LocalDate date, LocalTime time, Long cursor, int size) {
        StockChartMinute chart = stockChartProvider.findStockChartMinuteCreatedAtBy(stockCode, date, time);

        LocalDate chartDate = chart.getDate();
        LocalTime chartTime = chart.getTime();
        LocalDateTime start = LocalDateTime.of(chartDate, chartTime);
        LocalDateTime end = start.plusMinutes(1L);

        List<Comment> comments = commentRepository.findCommentsByStockCodeByCursorBetween(stockCode, cursor, size, start, end);

        return getCommentDtos(comments);
    }

    public List<CommentDto> getCommentListByHour(String stockCode, LocalDate date, LocalTime time, Long cursor, int size) {
        StockChartHourly chart = stockChartProvider.findStockChartHourlyCreatedAtBy(stockCode, date, time);

        LocalDate chartDate = chart.getDate();
        LocalTime chartTime = chart.getTime();
        LocalDateTime start = LocalDateTime.of(chartDate, chartTime);
        LocalDateTime end = start.plusHours(1L);

        List<Comment> comments = commentRepository.findCommentsByStockCodeByCursorBetween(stockCode, cursor, size, start, end);

        return getCommentDtos(comments);
    }

    public List<CommentDto> getCommentListByDaily(String stockCode, LocalDate date, Long cursor, int size) {
        StockChartDaily chart = stockChartProvider.findStockChartDailyCreatedAtBy(stockCode, date);

        LocalDate chartDate = chart.getDate();
        LocalDateTime start = chartDate.atStartOfDay();
        LocalDateTime end = start.plusDays(1L);

        List<Comment> comments = commentRepository.findCommentsByStockCodeByCursorBetween(stockCode, cursor, size, start, end);

        return getCommentDtos(comments);
    }

    public List<CommentDto> getCommentListByWeek(String stockCode, LocalDate date, Long cursor, int size) {
        StockChartWeekly chart = stockChartProvider.findStockChartWeeklyCreatedAtBy(stockCode, date);

        LocalDate chartDate = chart.getDate();
        LocalDateTime start = chartDate.atStartOfDay();
        LocalDateTime end = start.plusWeeks(1L);

        List<Comment> comments = commentRepository.findCommentsByStockCodeByCursorBetween(stockCode, cursor, size, start, end);

        return getCommentDtos(comments);
    }

    public List<CommentDto> getCommentListByMonth(String stockCode, Integer year, Integer month, Long cursor, int size) {
        StockChartMonthly chart = stockChartProvider.findStockChartMonthlyCreatedAtBy(stockCode, year, month);

        Integer chartYear = chart.getYear();
        Integer chartMonth = chart.getMonth();
        LocalDateTime start = LocalDateTime.of(chartYear, chartMonth, 1, 0, 0);
        LocalDateTime end = start.plusMonths(1L);

        List<Comment> comments = commentRepository.findCommentsByStockCodeByCursorBetween(stockCode, cursor, size, start, end);

        return getCommentDtos(comments);
    }

    public List<CommentDto> getChildCommentList(Long parentId, Long cursor, int size) {
        List<Comment> comments = commentRepository.findChildCommentsByParentIdByCursor(parentId, cursor, size);

        return getCommentDtos(comments);
    }

    @Retry
    public void increaseLikeCount(Long commentId) {
        Comment comment = commentRepository.findByIdWithOptimisticLock(commentId);
        comment.increaseLikeCount();
        commentRepository.save(comment);
    }

    @Retry
    public void decreaseLikeCount(Long commentId) {
        Comment comment = commentRepository.findByIdWithOptimisticLock(commentId);
        comment.decreaseLikeCount();
        commentRepository.save(comment);
    }

    private void validateStockCode(String stockCode) {
        if (!stockInfoRepository.existsByStockCode(stockCode)) {
            throw new CommentException(STOCK_CODE_NOT_FOUND);
        }
    }

    private UserComment create(Long userId, UserCommentCreateRequest request) {
        if (request.isAnonymous()) {
            return createAnonymousUserComment(userId, request);
        }
        return createIdentifiedUserComment(userId, request);
    }

    private UserComment createAnonymousUserComment(Long userId, UserCommentCreateRequest request) {
        return UserComment.create(
                request.stockCode(),
                request.content(),
                authorNameGenerator.generate(),
                request.parentId(),
                userId,
                true
        );
    }

    private static UserComment createIdentifiedUserComment(Long userId, UserCommentCreateRequest request) {
        return UserComment.create(
                request.stockCode(),
                request.content(),
                null,
                request.parentId(),
                userId,
                false
        );
    }

    private void validateParentId(Long parentId, Comment comment) {
        if (parentId != null) {
            Comment parent = commentRepository.findById(parentId);
            if (parent.isChild()) {
                throw new CommentException(PARENT_COMMENT_IS_CHILD_COMMENT);
            }
            parent.hasChild();
            comment.hasParent();
            commentRepository.save(parent);
        }
    }

    private GuestComment create(GuestCommentCreateRequest request) {
        return GuestComment.create(
                request.stockCode(),
                request.content(),
                request.parentId(),
                authorNameGenerator.generate(),
                passwordEncoder.encode(request.password())
        );
    }

    private boolean passwordUnmatched(String password, GuestComment guestComment) {
        return !passwordEncoder.matches(password, guestComment.getPassword());
    }

    private Map<Long, String> getNameMap(List<Comment> comments) {
        List<Long> userIds = comments
                .stream()
                .filter(comment -> comment instanceof UserComment)
                .filter(comment -> (!((UserComment) comment).isAnonymous()))
                .map(comment -> ((UserComment) comment).getUserId())
                .toList();
        return userRepository.findAllByIdIn(userIds)
                .stream()
                .collect(Collectors.toMap(User::getId, User::getName));
    }

    private List<CommentDto> getCommentDtos(List<Comment> comments) {
        Map<Long, String> nameMap = getNameMap(comments);

        return comments
                .stream()
                .map(comment -> {
                    if (comment instanceof UserComment userComment) {
                        if (userComment.isAnonymous()) {
                            return CommentDto.from(userComment);
                        }
                        return CommentDto.from(userComment, nameMap.get(userComment.getUserId()));
                    }

                    GuestComment guestComment = (GuestComment) comment;
                    return CommentDto.from(guestComment);
                }).toList();
    }

    private void deleteChildComment(Comment comment) {
        List<Long> childIds = commentRepository.findChildIdsByParentId(comment.getId());
        int count = commentRepository.deleteAllByIdIn(childIds);
        log.info("{}번 댓글에 포함된 자식 댓글 {}개 삭제", comment.getId(), count);
    }

    private void updateParentComment(Comment comment) {
        int count = commentRepository.countByParentId(comment.getParentId());
        log.info("{}번 댓글에 포함된 자식 댓글 {}개", comment.getParentId(), count);
        if (count > 1) {
            return;
        }
        Comment parent = commentRepository.findById(comment.getParentId());
        parent.isNotParent();
        commentRepository.save(parent);
    }
}
