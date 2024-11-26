package com.port90.core.comment.application;

import com.port90.core.comment.infrastructure.CommentRepository;
import com.port90.core.comment.infrastructure.PasswordEncoder;
import com.port90.core.comment.domain.exception.CommentException;
import com.port90.core.comment.domain.model.Comment;
import com.port90.core.comment.dto.request.CommentCreateRequest;
import com.port90.core.comment.dto.response.CommentCreateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.port90.core.comment.domain.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final GuestIdGenerator guestIdGenerator;
    private final PasswordEncoder passwordEncoder;

    public CommentCreateResponse create(Long userId, CommentCreateRequest request) {

        validateParentComment(request.parentId());

        Comment comment = commentRepository.save(createComment(userId, request));
        log.info("Comment ID: {} Created", comment.getId());

        return CommentCreateResponse.from(comment);
    }

    private void validateParentComment(Long parentId) {
        if (parentId == null) {
            return;
        }
        if (!commentRepository.existsByParentId(parentId)) {
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
                request.parentId());
    }
}
