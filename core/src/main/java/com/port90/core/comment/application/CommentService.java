package com.port90.core.comment.application;

import com.port90.core.comment.domain.exception.CommentException;
import com.port90.core.comment.domain.model.Comment;
import com.port90.core.comment.dto.request.CommentCreateRequest;
import com.port90.core.comment.dto.request.CommentDeleteRequest;
import com.port90.core.comment.dto.request.CommentUpdateRequest;
import com.port90.core.comment.dto.response.CommentCreateResponse;
import com.port90.core.comment.dto.response.CommentUpdatedResponse;
import com.port90.core.comment.infrastructure.CommentRepository;
import com.port90.core.comment.infrastructure.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.port90.core.comment.domain.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final GuestIdGenerator guestIdGenerator;
    private final PasswordEncoder passwordEncoder;

    public CommentCreateResponse create(Long userId, CommentCreateRequest request) {

        validateCreateComment(userId, request);

        Comment comment = commentRepository.save(createComment(userId, request));

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

        List<Long> commentIds = findAllChildCommentIds(comment.getId());
        int deletedCount = commentRepository.deleteAllByIdIn(commentIds);

        log.info("Comment Deleted Count: {}", deletedCount);
        log.info("Comment ID: {} Deleted", comment.getId());
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
    }

    private Comment createComment(Long userId, CommentCreateRequest request) {
        return userId != null
                ? Comment.createByUser(userId, request.content(), request.parentId())
                : Comment.createByGuest(
                guestIdGenerator.generate(),
                passwordEncoder.encode(request.guestPassword()),
                request.content(),
                request.parentId()
        );
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
}
