package com.port90.core.reply.infrastructure.impl.repository;

import com.port90.core.reply.domain.error.ReplyException;
import com.port90.core.reply.domain.model.CommentReplyCount;
import com.port90.core.reply.domain.model.Reply;
import com.port90.core.reply.infrastructure.ReplyRepository;
import com.port90.core.reply.infrastructure.impl.repository.persistence.ReplyJpaRepository;
import com.port90.core.reply.infrastructure.impl.repository.persistence.ReplyQueryRepository;
import com.port90.core.reply.infrastructure.impl.repository.persistence.mapper.ReplyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.port90.core.reply.domain.error.ReplyErrorCode.REPLY_NOT_FOUND_BY_ID;

@Repository
@RequiredArgsConstructor
public class ReplyRepositoryImpl implements ReplyRepository {

    private final ReplyJpaRepository replyJpaRepository;
    private final ReplyQueryRepository replyQueryRepository;

    @Override
    public Reply save(Reply reply) {
        return ReplyMapper.toModel(
                replyJpaRepository.save(
                        ReplyMapper.toEntity(reply)
                )
        );
    }

    @Override
    public Reply saveAndFlush(Reply reply) {
        return ReplyMapper.toModel(
                replyJpaRepository.saveAndFlush(
                        ReplyMapper.toEntity(reply)
                )
        );
    }

    @Override
    public Reply getById(Long replyId) {
        return ReplyMapper.toModel(
                replyJpaRepository.findById(replyId)
                        .orElseThrow(() -> new ReplyException(REPLY_NOT_FOUND_BY_ID, replyId))
        );
    }

    @Override
    public void delete(Reply reply) {
        replyJpaRepository.delete(
                ReplyMapper.toEntity(reply)
        );
    }

    @Override
    public int deleteByCommentId(Long commentId) {
        return replyJpaRepository.deleteByCommentId(commentId);
    }

    @Override
    public List<Reply> findAllByCommentId(Long commentId) {
        return replyJpaRepository.findAllByCommentId(commentId)
                .stream()
                .map(ReplyMapper::toModel)
                .toList();
    }

    @Override
    public Map<Long, Long> getCommentIdToReplyCountMap(Set<Long> commentIds) {
        if (commentIds.isEmpty()) {
            return new HashMap<>();
        }

        return replyQueryRepository.getCommentReplyCountList(commentIds)
                .stream()
                .collect(Collectors.toMap(
                        CommentReplyCount::commentId,
                        CommentReplyCount::replyCount
                ));
    }
}
