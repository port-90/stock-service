package com.port90.core.like.application;

import com.port90.core.comment.application.CommentService;
import com.port90.core.like.domain.exception.LikeException;
import com.port90.core.like.domain.model.Like;
import com.port90.core.like.dto.request.LikeCreateRequest;
import com.port90.core.like.dto.response.LikeCreateResponse;
import com.port90.core.like.infrastructure.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.port90.core.like.domain.exception.LikeErrorCode.LIKE_ALREADY_EXISTS;
import static com.port90.core.like.domain.exception.LikeErrorCode.LIKE_USER_UNMATCHED;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final CommentService commentService;

    @Transactional
    public LikeCreateResponse create(Long userId, LikeCreateRequest request) {

        checkLikeAlreadyExists(userId, request);

        Like like = likeRepository.save(Like.create(userId, request.commentId()));

        commentService.increaseLikeCount(like.getCommentId());

        return LikeCreateResponse.from(like);
    }

    @Transactional
    public void delete(Long userId, Long likeId) {

        Like like = likeRepository.findById(likeId);
        if (like.isNotLikedBy(userId)) {
            throw new LikeException(LIKE_USER_UNMATCHED);
        }
        likeRepository.deleteById(like.getId());

        commentService.decreaseLikeCount(like.getCommentId());
    }

    private void checkLikeAlreadyExists(Long userId, LikeCreateRequest request) {
        if (likeRepository.existsByUserIdAndCommentId(userId, request.commentId())) {
            throw new LikeException(LIKE_ALREADY_EXISTS);
        }
    }
}
