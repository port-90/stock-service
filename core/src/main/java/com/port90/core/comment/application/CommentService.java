package com.port90.core.comment.application;

import com.port90.core.auth.domain.model.User;
import com.port90.core.auth.infrastructure.UserRepository;
import com.port90.core.comment.aop.Retry;
import com.port90.core.comment.domain.exception.CommentException;
import com.port90.core.comment.domain.model.Comment;
import com.port90.core.comment.dto.CommentDto;
import com.port90.core.comment.dto.request.CommentCreateRequest;
import com.port90.core.comment.dto.request.CommentDeleteRequest;
import com.port90.core.comment.dto.request.CommentUpdateRequest;
import com.port90.core.comment.dto.response.CommentCreateResponse;
import com.port90.core.comment.dto.response.CommentUpdatedResponse;
import com.port90.core.comment.infrastructure.CommentRepository;
import com.port90.stockdomain.infrastructure.StockInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    @Transactional
    public CommentCreateResponse create(Long userId, CommentCreateRequest request) {

        validateGuestPasswordRequired(userId, request);
        validateStockCode(request);

        Comment comment = createComment(userId, request);

        checkParentCommentAndUpdate(request, comment);

        commentRepository.save(comment);

        log.info("Comment ID: {} Created", comment.getId());

        return CommentCreateResponse.from(comment);
    }

    @Transactional
    public CommentUpdatedResponse update(Long userId, Long commentId, CommentUpdateRequest request) {
        Comment comment = commentRepository.findById(commentId);

        validateComment(userId, request.guestPassword(), comment);

        comment.updateContent(request.content());

        commentRepository.save(comment);

        log.info("Comment ID: {} Updated", comment.getId());

        return CommentUpdatedResponse.from(comment);
    }

    @Transactional
    public void delete(Long userId, Long commentId, CommentDeleteRequest request) {
        Comment comment = commentRepository.findById(commentId);

        validateComment(userId, request.guestPassword(), comment);

        if (comment.isParent()) {
            deleteChildComment(comment);
        }
        if (comment.isChild()) {
            updateParentCommentIsNotParentAnymore(comment);
        }

        commentRepository.delete(comment);

        log.info("Comment ID: {} Deleted", comment.getId());
    }

    public List<CommentDto> getCommentList(String stockCode, Long cursor, int size) {
        List<Comment> comments = commentRepository.findCommentsByStockCodeByCursor(stockCode, cursor, size);

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

    private boolean guessPasswordUnmatched(String guestPassword, Comment comment) {
        return !passwordEncoder.matches(guestPassword, comment.getGuestPassword());
    }

    private Map<Long, String> getNameMap(List<Comment> comments) {
        List<Long> userIds = comments
                .stream()
                .map(Comment::getUserId)
                .filter(Objects::nonNull)
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
                    if (comment.isGuestComment() || comment.isAnonymousUserComment()) {
                        return CommentDto.fromGuestOrAnonymousUser(comment, authorNameGenerator.generate());
                    }
                    return CommentDto.fromUser(comment, nameMap.get(comment.getUserId()));
                }).toList();
    }

    private void validateStockCode(CommentCreateRequest request) {
        if (!stockInfoRepository.existsByStockCode(request.stockCode())) {
            throw new CommentException(STOCK_CODE_NOT_FOUND);
        }
    }

    private static void validateGuestPasswordRequired(Long userId, CommentCreateRequest request) {
        if (userId == null && request.guestPassword() == null) {
            throw new CommentException(GUEST_PASSWORD_REQUIRED);
        }
    }

    private static Comment createComment(Long userId, CommentCreateRequest request) {
        if (requestedByUser(userId, request)) {
            if (isAnonymousUserComment(request)) {
                return Comment.createByAnonymousUser(userId, request.stockCode(), request.content(), request.parentId());
            }

            return Comment.createByUser(userId, request.stockCode(), request.content(), request.parentId());
        }
        if (requestedByGuest(userId, request)) {
            return Comment.createByGuest(request.stockCode(), request.guestPassword(), request.content(), request.parentId());
        }
        throw new CommentException(INVALID_COMMENT);
    }

    private static boolean requestedByGuest(Long userId, CommentCreateRequest request) {
        return userId == null && request.guestPassword() != null;
    }

    private static boolean isAnonymousUserComment(CommentCreateRequest request) {
        return request.isAnonymousComment();
    }

    private static boolean requestedByUser(Long userId, CommentCreateRequest request) {
        return userId != null && request.guestPassword() == null;
    }

    private void checkParentCommentAndUpdate(CommentCreateRequest request, Comment comment) {
        if (request.parentId() != null) {
            Comment parent = commentRepository.findById(request.parentId());
            if (parent.isChild()) {
                throw new CommentException(PARENT_COMMENT_IS_CHILD_COMMENT);
            }
            parent.hasChild();
            comment.hasParent();
            commentRepository.save(parent);
        }
    }

    private void validateComment(Long userId, String guestPassword, Comment comment) {
        if (comment.isUserComment() || comment.isAnonymousUserComment()) {
            if (comment.isNotWrittenBy(userId)) {
                throw new CommentException(COMMENT_USER_UNMATCHED);
            }
        }
        if (comment.isGuestComment()) {
            if (guestPassword == null) {
                throw new CommentException(GUEST_PASSWORD_REQUIRED);
            }
            if (guessPasswordUnmatched(guestPassword, comment)) {
                throw new CommentException(GUEST_PASSWORD_UNMATCHED);
            }
        }
    }

    private void deleteChildComment(Comment comment) {
        List<Long> childIds = commentRepository.findChildIdsByParentId(comment.getId());
        int count = commentRepository.deleteAllByIdIn(childIds);
        log.info("{}번 댓글에 포함된 자식 댓글 {}개 삭제", comment.getId(), count);
    }

    private void updateParentCommentIsNotParentAnymore(Comment comment) {
        Comment parent = commentRepository.findById(comment.getParentId());
        parent.isNotParent();
        commentRepository.save(parent);
    }
}
