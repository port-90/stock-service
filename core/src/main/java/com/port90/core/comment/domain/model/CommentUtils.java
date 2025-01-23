package com.port90.core.comment.domain.model;

import java.util.Optional;

import static com.port90.core.comment.domain.model.CommentType.*;

public class CommentUtils {

    public static CommentType resolveType(CommentCreate commentCreate) {
        if (commentCreate.userId() == null) {
            return UNAUTHENTICATED;
        }
        Boolean isAnonymous = Optional.ofNullable(commentCreate.isAnonymous()).orElse(false);
        return isAnonymous ? AUTHENTICATED_ANONYMOUS : AUTHENTICATED;
    }
}
