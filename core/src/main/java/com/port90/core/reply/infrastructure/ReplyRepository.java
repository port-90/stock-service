package com.port90.core.reply.infrastructure;

import com.port90.core.reply.domain.model.Reply;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ReplyRepository {
    Reply save(Reply reply);

    Reply saveAndFlush(Reply reply);

    Reply getById(Long replyId);

    void delete(Reply reply);

    int deleteByCommentId(Long commentId);

    List<Reply> findAllByCommentId(Long commentId);

    Map<Long, Long> getCommentIdToReplyCountMap(Set<Long> commentIds);
}
