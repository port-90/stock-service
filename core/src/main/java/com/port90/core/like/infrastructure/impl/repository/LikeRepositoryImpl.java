package com.port90.core.like.infrastructure.impl.repository;

import com.port90.core.like.domain.exception.LikeException;
import com.port90.core.like.domain.model.Like;
import com.port90.core.like.infrastructure.LikeRepository;
import com.port90.core.like.infrastructure.impl.repository.persistence.LikeJpaRepository;
import com.port90.core.like.infrastructure.impl.repository.persistence.mapper.LikeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.port90.core.like.domain.exception.LikeErrorCode.LIKE_NOT_FOUND;

@Repository
@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepository {

    private final LikeJpaRepository likeJpaRepository;

    @Override
    public Like save(Like like) {
        return LikeMapper.toModel(
                likeJpaRepository.save(
                        LikeMapper.toEntity(like)
                )
        );
    }

    @Override
    public boolean existsByUserIdAndCommentId(Long userId, Long commentId) {
        return likeJpaRepository.existsByUserIdAndCommentId(userId, commentId);
    }

    @Override
    public Like findById(Long likeId) {
        return likeJpaRepository.findById(likeId)
                .map(LikeMapper::toModel)
                .orElseThrow(() -> new LikeException(LIKE_NOT_FOUND));
    }

    @Override
    public void deleteById(Long likeId) {
        likeJpaRepository.deleteById(likeId);
    }
}
