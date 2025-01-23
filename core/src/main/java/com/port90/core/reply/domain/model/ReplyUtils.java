package com.port90.core.reply.domain.model;

import java.util.Optional;

import static com.port90.core.reply.domain.model.ReplyType.*;

public class ReplyUtils {

    public static ReplyType resolveType(ReplyCreate replyCreate) {
        if (replyCreate.userId() == null) {
            return UNAUTHENTICATED;
        }
        Boolean isAnonymous = Optional.ofNullable(replyCreate.isAnonymous()).orElse(false);
        return isAnonymous ? AUTHENTICATED_ANONYMOUS : AUTHENTICATED;
    }
}
