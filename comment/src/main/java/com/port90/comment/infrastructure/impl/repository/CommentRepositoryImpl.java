package com.port90.comment.infrastructure.impl.repository;

import com.port90.comment.infrastructure.CommentRepository;
import com.port90.comment.domain.model.Comment;
import com.port90.comment.infrastructure.impl.repository.persistence.CommentJpaRepository;
import com.port90.comment.infrastructure.impl.repository.persistence.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {

    private final CommentJpaRepository commentJpaRepository;

    @Override
    public Comment save(Comment comment) {
        return CommentMapper.toModel(
                commentJpaRepository.save(
                        CommentMapper.toEntity(comment)
                )
        );
    }

    @Override
    public boolean existsByParentId(Long parentId) {
        return commentJpaRepository.existsById(parentId);
    }
}
