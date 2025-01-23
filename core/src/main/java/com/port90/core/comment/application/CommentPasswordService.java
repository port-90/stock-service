package com.port90.core.comment.application;

import com.port90.core.comment.domain.error.CommentException;
import com.port90.core.comment.domain.model.Comment;
import com.port90.core.comment.domain.model.CommentCreate;
import com.port90.core.common.infrastructure.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.port90.core.comment.domain.error.CommentErrorCode.COMMENT_PASSWORD_MISMATCH;
import static com.port90.core.comment.domain.error.CommentErrorCode.COMMENT_PASSWORD_REQUIRED;

@Service
@RequiredArgsConstructor
public class CommentPasswordService {

    private final PasswordEncoder passwordEncoder;

    public String encodePassword(CommentCreate commentCreate) {
        if (commentCreate.passwordIsNull()) {
            throw new CommentException(COMMENT_PASSWORD_REQUIRED);
        }
        return passwordEncoder.encode(commentCreate.password());
    }

    public void validatePassword(String password, Comment comment) {
        if (password == null) {
            throw new CommentException(COMMENT_PASSWORD_REQUIRED);
        }
        if (!passwordEncoder.matches(password, comment.getPassword())) {
            throw new CommentException(COMMENT_PASSWORD_MISMATCH);
        }
    }
}
