package com.port90.core.like.infrastructure.impl.repository;

import com.port90.core.like.domain.error.LikeException;
import com.port90.core.like.domain.model.Like;
import com.port90.core.like.domain.model.LikeTarget;
import com.port90.core.like.infrastructure.LikeRepository;
import com.port90.core.like.infrastructure.impl.repository.persistence.LikeJpaRepository;
import com.port90.core.like.infrastructure.impl.repository.persistence.mapper.LikeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.port90.core.like.domain.error.LikeErrorCode.LIKE_NOT_FOUND_BY_ID;

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
    public void delete(Like like) {
        likeJpaRepository.delete(
                LikeMapper.toEntity(like)
        );
    }

    @Override
    public Like getById(Long likeId) {
        return likeJpaRepository.findById(likeId)
                .map(LikeMapper::toModel)
                .orElseThrow(() -> new LikeException(LIKE_NOT_FOUND_BY_ID, likeId));
    }

    @Override
    public boolean existsByUserIdAndTargetAndTargetId(Long userId, LikeTarget target, Long targetId) {
        return likeJpaRepository.existsByUserIdAndTargetAndTargetId(userId, target, targetId);
    }
}
