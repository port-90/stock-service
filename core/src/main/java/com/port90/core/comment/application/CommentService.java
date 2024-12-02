package com.port90.core.comment.application;

import com.port90.core.auth.domain.model.User;
import com.port90.core.auth.infrastructure.UserRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.port90.core.comment.domain.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final GuestIdGenerator guestIdGenerator;
    private final PasswordEncoder passwordEncoder;
    private final StockInfoRepository stockInfoRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentCreateResponse create(Long userId, CommentCreateRequest request) {

        validateCreateComment(userId, request);

        Comment comment = commentRepository.save(createComment(userId, request));

        checkIfParentExists(request);

        log.info("Comment ID: {} Created", comment.getId());

        return CommentCreateResponse.from(comment);
    }

    @Transactional
    public CommentUpdatedResponse update(Long userId, Long commentId, CommentUpdateRequest request) {
        Comment comment = commentRepository.findById(commentId);

        validateUserComment(userId, comment);
        validateGuestComment(request.guestPassword(), comment);

        comment.updateContent(request.content());
        commentRepository.save(comment);

        log.info("Comment ID: {} Updated", comment.getId());

        return CommentUpdatedResponse.from(comment);
    }

    @Transactional
    public void delete(Long userId, Long commentId, CommentDeleteRequest request) {
        Comment comment = commentRepository.findById(commentId);

        validateUserComment(userId, comment);
        validateGuestComment(request.guestPassword(), comment);

        updateParent(comment);

        List<Long> commentIds = findAllChildCommentIds(comment.getId());
        int deletedCount = commentRepository.deleteAllByIdIn(commentIds);

        log.info("Comment Deleted Count: {}", deletedCount);
        log.info("Comment ID: {} Deleted", comment.getId());
    }

    public List<CommentDto> getParentCommentList(String stockCode, Long cursor, int size) {
        List<Comment> comments = commentRepository.findParentCommentsByStockCodeByCursor(stockCode, cursor, size);

        return getCommentDtos(comments);
    }

    public List<CommentDto> getChildCommentList(Long parentId, Long cursor, int size) {
        List<Comment> comments = commentRepository.findChildCommentsByParentIdByCursor(parentId, cursor, size);

        return getCommentDtos(comments);
    }

    private List<Long> findAllChildCommentIds(Long commentId) {
        List<Long> commentIds = new ArrayList<>();
        commentIds.add(commentId);

        List<Long> childIds = commentRepository.findChildIdsByParentId(commentId);
        for (Long childId : childIds) {
            commentIds.addAll(findAllChildCommentIds(childId));
        }

        return commentIds;
    }

    private void validateCreateComment(Long userId, CommentCreateRequest request) {
        if (userId == null && request.guestPassword() == null) {
            throw new CommentException(GUEST_PASSWORD_REQUIRED);
        }
        if (request.parentId() != null && !commentRepository.existsByParentId(request.parentId())) {
            throw new CommentException(PARENT_NOT_FOUND);
        }
        if (!stockInfoRepository.existsByStockCode(request.stockCode())) {
            throw new CommentException(STOCK_CODE_NOT_FOUND);
        }
    }

    private Comment createComment(Long userId, CommentCreateRequest request) {
        return userId != null
                ? Comment.createByUser(userId, request.stockCode(), request.content(), request.parentId())
                : Comment.createByGuest(
                request.stockCode(),
                guestIdGenerator.generate(),
                passwordEncoder.encode(request.guestPassword()),
                request.content(),
                request.parentId()
        );
    }

    private void checkIfParentExists(CommentCreateRequest request) {
        if (request.parentId() != null) {
            Comment parent = commentRepository.findById(request.parentId());
            parent.existChildren();
            commentRepository.save(parent);
        }
    }

    private void validateUserComment(Long userId, Comment comment) {
        if (comment.isUserComment() && comment.isNotCreatedBy(userId)) {
            throw new CommentException(COMMENT_USER_UNMATCHED);
        }
    }

    private void validateGuestComment(String guestPassword, Comment comment) {
        if (comment.isGuestComment()) {
            if (guestPassword == null) {
                throw new CommentException(GUEST_PASSWORD_REQUIRED);
            }
            if (guessPasswordUnmatched(guestPassword, comment)) {
                throw new CommentException(GUEST_PASSWORD_UNMATCHED);
            }
        }
    }

    private boolean guessPasswordUnmatched(String guestPassword, Comment comment) {
        return !passwordEncoder.matches(guestPassword, comment.getGuestPassword());
    }

    private void updateParent(Comment comment) {
        if (comment.hasParent()) {
            long count = commentRepository.countByParentId(comment.getParentId());
            if (count > 1) {
                return;
            }

            Comment parent = commentRepository.findById(comment.getParentId());
            parent.nonExistChildren();
            commentRepository.save(parent);
        }
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
                    if (comment.isGuestComment()) {
                        return CommentDto.fromGuest(comment);
                    }
                    return CommentDto.fromUser(comment, nameMap.get(comment.getUserId()));
                }).toList();
    }
}
