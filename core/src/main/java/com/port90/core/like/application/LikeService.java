package com.port90.core.like.application;

import com.port90.core.comment.application.CommentService;
import com.port90.core.like.domain.error.LikeException;
import com.port90.core.like.domain.model.Like;
import com.port90.core.like.domain.model.LikeCreate;
import com.port90.core.like.domain.model.LikeDelete;
import com.port90.core.like.infrastructure.LikeRepository;
import com.port90.core.reply.application.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.port90.core.like.domain.error.LikeErrorCode.LIKE_ALREADY_EXISTS;
import static com.port90.core.like.domain.error.LikeErrorCode.LIKE_USER_MISMATCH;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final CommentService commentService;
    private final ReplyService replyService;

    @Transactional
    public Like create(LikeCreate likeCreate) {

        checkLikeAlreadyExists(likeCreate);

        if (likeCreate.isCommentLikeCreate()) {
            commentService.increaseLikeCount(likeCreate.targetId());
        }
        if (likeCreate.isReplyLikeCreate()) {
            replyService.increaseLikeCount(likeCreate.targetId());
        }

        return likeRepository.save(Like.create(likeCreate));
    }

    @Transactional
    public void delete(LikeDelete likeDelete) {

        Like like = likeRepository.getById(likeDelete.likeId());

        if (like.validateUserId(likeDelete)) {
            throw new LikeException(LIKE_USER_MISMATCH);
        }

        if (like.isCommentLike()) {
            commentService.decreaseLikeCount(like.getTargetId());
        }
        if (like.isReplyLike()) {
            replyService.decreaseLikeCount(like.getTargetId());
        }

        likeRepository.delete(like);
    }

    private void checkLikeAlreadyExists(LikeCreate likeCreate) {
        if (likeRepository.existsByUserIdAndTargetAndTargetId(likeCreate.userId(), likeCreate.target(), likeCreate.targetId())) {
            throw new LikeException(LIKE_ALREADY_EXISTS);
        }
    }
}
