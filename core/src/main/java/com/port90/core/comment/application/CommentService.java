package com.port90.core.comment.application;

import com.port90.core.auth.domain.exception.UserException;
import com.port90.core.auth.infrastructure.UserRepository;
import com.port90.core.comment.domain.error.CommentException;
import com.port90.core.comment.domain.model.*;
import com.port90.core.comment.infrastructure.CommentRepository;
import com.port90.core.common.aop.Retry;
import com.port90.core.reply.application.ReplyService;
import com.port90.core.stockchart.application.StockChartService;
import com.port90.core.stockchart.domain.StockChartMinuteId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.port90.core.auth.domain.exception.ErrorCode.USER_NOT_FOUND_BY_ID;
import static com.port90.core.comment.domain.error.CommentErrorCode.COMMENT_WRITER_AUTHENTICATION_REQUIRED;
import static com.port90.core.comment.domain.error.CommentErrorCode.UNSUPPORTED_COMMENT_TYPE;
import static com.port90.core.comment.domain.model.CommentType.*;
import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final StockChartService stockChartService;
    private final CommentPasswordService passwordService;
    private final ReplyService replyService;

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public Comment create(CommentCreate commentCreate) {
        StockChartMinuteId stockChartMinuteId = stockChartService.getLatestStockChartMinuteIdByStockCode(commentCreate.stockCode());

        CommentType type = CommentUtils.resolveType(commentCreate);

        Comment comment = buildCommentByType(commentCreate, type, stockChartMinuteId);

        return commentRepository.save(comment);
    }

    public Comment update(CommentUpdate commentUpdate) {
        Comment comment = commentRepository.getById(commentUpdate.commentId());

        if (comment.isUnauthenticated()) {
            passwordService.validatePassword(commentUpdate.password(), comment);
        }
        if (comment.isAuthenticated() || comment.isAuthenticatedAnonymous()) {
            if (commentUpdate.userIdIsNull()) {
                throw new CommentException(COMMENT_WRITER_AUTHENTICATION_REQUIRED);
            }
            comment.validateUserId(commentUpdate.userId());
        }

        comment.updateContent(commentUpdate);

        return commentRepository.save(comment);
    }

    @Transactional
    public void delete(CommentDelete commentDelete) {
        Comment comment = commentRepository.getById(commentDelete.commentId());

        if (comment.isUnauthenticated()) {
            passwordService.validatePassword(commentDelete.password(), comment);
        }
        if (comment.isAuthenticated() || comment.isAuthenticatedAnonymous()) {
            if (commentDelete.userIdIsNull()) {
                throw new CommentException(COMMENT_WRITER_AUTHENTICATION_REQUIRED);
            }
            comment.validateUserId(commentDelete.userId());
        }

        replyService.deleteByCommentId(comment.getId());

        commentRepository.delete(comment);
    }

    public Page<Comment> getListByUserId(Long userId, Pageable pageable) {
        return commentRepository.findAllByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    public List<CommentDto> getListByStockCode(String stockCode, Long cursor, int size) {
        List<Comment> comments = commentRepository.findAllByStockCodeByCursor(stockCode, cursor, size);

        return getCommentDtoList(comments);
    }

    public List<CommentDto> getListInStockChartMinute(String stockCode, LocalDate date, LocalTime time, Long cursor, int size) {
        List<Comment> comments = commentRepository.findAllByStockCodeAndDateAndTimeByCursor(stockCode, date, time, cursor, size);

        return getCommentDtoList(comments);
    }

    public List<CommentDto> getListInStockChartHourly(String stockCode, LocalDate date, LocalTime time, Long cursor, int size) {
        LocalTime startTime = time.minusHours(1);
        List<Comment> comments = commentRepository.findAllByStockCodeAndDateAndTimeBetweenByCursor(stockCode, date, startTime, time, cursor, size);

        return getCommentDtoList(comments);
    }

    public List<CommentDto> getListInStockChartDaily(String stockCode, LocalDate date, Long cursor, int size) {
        List<Comment> comments = commentRepository.findAllByStockCodeAndDateByCursor(stockCode, date, cursor, size);

        return getCommentDtoList(comments);
    }

    public List<CommentDto> getListInStockChartWeekly(String stockCode, LocalDate date, Long cursor, int size) {
        LocalDate startOfWeek = date.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = date.with(DayOfWeek.SUNDAY);
        List<Comment> comments = commentRepository.findAllByStockCodeAndDateBetweenByCursor(stockCode, startOfWeek, endOfWeek, cursor, size);

        return getCommentDtoList(comments);
    }

    public List<CommentDto> getListInStockChartMonthly(String stockCode, Integer year, Integer month, Long cursor, int size) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startOfMonth = yearMonth.atDay(1);
        LocalDate endOfMonth = yearMonth.atEndOfMonth();
        List<Comment> comments = commentRepository.findAllByStockCodeAndDateBetweenByCursor(stockCode, startOfMonth, endOfMonth, cursor, size);

        return getCommentDtoList(comments);
    }

    @Retry
    @Transactional(propagation = REQUIRES_NEW)
    public void increaseLikeCount(Long commentId) {
        Comment comment = commentRepository.getById(commentId);
        comment.increaseLikeCount();
        commentRepository.saveAndFlush(comment);
    }

    @Retry
    @Transactional(propagation = REQUIRES_NEW)
    public void decreaseLikeCount(Long commentId) {
        Comment comment = commentRepository.getById(commentId);
        comment.decreaseLikeCount();
        commentRepository.saveAndFlush(comment);
    }

    private Comment buildCommentByType(CommentCreate commentCreate, CommentType type, StockChartMinuteId stockChartMinuteId) {
        if (type == UNAUTHENTICATED) {
            String password = passwordService.encodePassword(commentCreate);
            return Comment.createUnAuthenticated(commentCreate, type, password, stockChartMinuteId);
        }
        if (type == AUTHENTICATED || type == AUTHENTICATED_ANONYMOUS) {
            if (!userRepository.existsById(commentCreate.userId())) {
                throw new UserException(USER_NOT_FOUND_BY_ID, commentCreate.userId());
            }
            return Comment.createAuthenticated(commentCreate, type, stockChartMinuteId);
        }
        throw new CommentException(UNSUPPORTED_COMMENT_TYPE);
    }

    private Map<Long, Long> getCommentIdToReplyCountMap(List<Comment> comments) {
        Set<Long> commentIds = comments.stream()
                .map(Comment::getId)
                .collect(Collectors.toSet());

        return replyService.getCommentIdToReplyCountMap(commentIds);
    }

    private Map<Long, String> getUserIdToNameMap(List<Comment> comments) {
        Set<Long> userIds = comments.stream()
                .filter(Comment::isAuthenticated)
                .map(Comment::getUserId)
                .collect(Collectors.toSet());

        if (!userIds.isEmpty()) {
            return userRepository.getUserIdToNameMap(userIds);
        }

        return null;
    }

    private List<CommentDto> getCommentDtoList(List<Comment> comments) {
        Map<Long, Long> commentIdToReplyCountMap = getCommentIdToReplyCountMap(comments);
        Map<Long, String> userIdToNameMap = getUserIdToNameMap(comments);

        return comments.stream()
                .map(comment -> {
                    if (comment.isUnauthenticated() || comment.isAuthenticatedAnonymous()) {
                        return CommentDto.from(
                                comment,
                                "익명",
                                commentIdToReplyCountMap.getOrDefault(comment.getId(), 0L)
                        );
                    }
                    assert userIdToNameMap != null;
                    return CommentDto.from(
                            comment,
                            userIdToNameMap.get(comment.getUserId()),
                            commentIdToReplyCountMap.getOrDefault(comment.getId(), 0L)
                    );
                }).toList();
    }
}
