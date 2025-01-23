package com.port90.core.reply.application;

import com.port90.core.common.infrastructure.PasswordEncoder;
import com.port90.core.reply.domain.error.ReplyException;
import com.port90.core.reply.domain.model.Reply;
import com.port90.core.reply.domain.model.ReplyCreate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.port90.core.reply.domain.error.ReplyErrorCode.REPLY_PASSWORD_MISMATCH;
import static com.port90.core.reply.domain.error.ReplyErrorCode.REPLY_PASSWORD_REQUIRED;

@Service
@RequiredArgsConstructor
public class ReplyPasswordService {

    private final PasswordEncoder passwordEncoder;

    public String encodePassword(ReplyCreate replyCreate) {
        if (replyCreate.passwordIsNull()) {
            throw new ReplyException(REPLY_PASSWORD_REQUIRED);
        }
        return passwordEncoder.encode(replyCreate.password());
    }

    public void validatePassword(String password, Reply reply) {
        if (password == null) {
            throw new ReplyException(REPLY_PASSWORD_REQUIRED);
        }
        if (!passwordEncoder.matches(password, reply.getPassword())) {
            throw new ReplyException(REPLY_PASSWORD_MISMATCH);
        }
    }
}
