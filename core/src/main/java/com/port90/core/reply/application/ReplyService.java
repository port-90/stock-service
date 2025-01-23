package com.port90.core.reply.application;

import com.port90.core.auth.domain.exception.UserException;
import com.port90.core.auth.infrastructure.UserRepository;
import com.port90.core.comment.domain.error.CommentException;
import com.port90.core.comment.infrastructure.CommentRepository;
import com.port90.core.common.aop.Retry;
import com.port90.core.reply.domain.error.ReplyException;
import com.port90.core.reply.domain.model.*;
import com.port90.core.reply.infrastructure.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.port90.core.auth.domain.exception.ErrorCode.USER_NOT_FOUND_BY_ID;
import static com.port90.core.comment.domain.error.CommentErrorCode.COMMENT_NOT_FOUND_BY_ID;
import static com.port90.core.reply.domain.error.ReplyErrorCode.REPLY_WRITER_AUTHENTICATION_REQUIRED;
import static com.port90.core.reply.domain.error.ReplyErrorCode.UNSUPPORTED_REPLY_TYPE;
import static com.port90.core.reply.domain.model.ReplyType.*;
import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReplyService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ReplyPasswordService replyPasswordService;
    private final ReplyRepository replyRepository;

    public Reply create(ReplyCreate replyCreate) {
        if (!commentRepository.existsById(replyCreate.commentId())) {
            throw new CommentException(COMMENT_NOT_FOUND_BY_ID, replyCreate.commentId());
        }

        ReplyType replyType = ReplyUtils.resolveType(replyCreate);

        Reply reply = buildReplyByType(replyCreate, replyType);

        return replyRepository.save(reply);
    }

    public Reply update(ReplyUpdate replyUpdate) {
        Reply reply = replyRepository.getById(replyUpdate.replyId());

        if (reply.isUnauthenticated()) {
            replyPasswordService.validatePassword(replyUpdate.password(), reply);
        }
        if (reply.isAuthenticated() || reply.isAuthenticatedAnonymous()) {
            if (replyUpdate.userIdIsNull()) {
                throw new ReplyException(REPLY_WRITER_AUTHENTICATION_REQUIRED);
            }
            reply.validateUserId(replyUpdate.userId());
        }

        reply.updateContent(replyUpdate);

        return replyRepository.save(reply);
    }

    public void delete(ReplyDelete replyDelete) {
        Reply reply = replyRepository.getById(replyDelete.replyId());

        if (reply.isUnauthenticated()) {
            replyPasswordService.validatePassword(replyDelete.password(), reply);
        }
        if (reply.isAuthenticated() || reply.isAuthenticatedAnonymous()) {
            if (replyDelete.userIdIsNull()) {
                throw new ReplyException(REPLY_WRITER_AUTHENTICATION_REQUIRED);
            }
            reply.validateUserId(replyDelete.userId());
        }

        replyRepository.delete(reply);
    }

    public void deleteByCommentId(Long commentId) {
        int count = replyRepository.deleteByCommentId(commentId);
        log.info("Comment Id {} 댓글에 포함된 답글 {}개 삭제", commentId, count);
    }

    public List<ReplyDto> getListByCommentId(Long commentId) {
        List<Reply> replies = replyRepository.findAllByCommentId(commentId);

        Map<Long, String> userIdToNameMap = getUserIdToNameMap(replies);

        return replies.stream()
                .map(reply -> {
                    if (reply.isUnauthenticated() || reply.isAuthenticatedAnonymous()) {
                        return ReplyDto.from(reply, "익명");
                    }
                    assert userIdToNameMap != null;
                    return ReplyDto.from(reply, userIdToNameMap.get(reply.getUserId()));
                }).toList();
    }

    public Map<Long, Long> getCommentIdToReplyCountMap(Set<Long> commentIds) {
        return replyRepository.getCommentIdToReplyCountMap(commentIds);
    }

    @Retry
    @Transactional(propagation = REQUIRES_NEW)
    public void increaseLikeCount(Long replyId) {
        Reply reply = replyRepository.getById(replyId);
        reply.increaseLikeCount();
        replyRepository.saveAndFlush(reply);
    }

    @Retry
    @Transactional(propagation = REQUIRES_NEW)
    public void decreaseLikeCount(Long replyId) {
        Reply reply = replyRepository.getById(replyId);
        reply.decreaseLikeCount();
        replyRepository.saveAndFlush(reply);
    }

    private Reply buildReplyByType(ReplyCreate replyCreate, ReplyType replyType) {
        if (replyType == UNAUTHENTICATED) {
            String password = replyPasswordService.encodePassword(replyCreate);
            return Reply.createUnAuthenticated(replyCreate, replyType, password);
        }
        if (replyType == AUTHENTICATED || replyType == AUTHENTICATED_ANONYMOUS) {
            if (!userRepository.existsById(replyCreate.userId())) {
                throw new UserException(USER_NOT_FOUND_BY_ID, replyCreate.userId());
            }
            return Reply.createAuthenticated(replyCreate, replyType);
        }
        throw new ReplyException(UNSUPPORTED_REPLY_TYPE);
    }

    private Map<Long, String> getUserIdToNameMap(List<Reply> replies) {
        Set<Long> userIds = replies.stream()
                .filter(Reply::isAuthenticated)
                .map(Reply::getUserId)
                .collect(Collectors.toSet());

        if (!userIds.isEmpty()) {
            return userRepository.getUserIdToNameMap(userIds);
        }

        return null;
    }
}
