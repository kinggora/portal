package kinggora.portal.service;

import kinggora.portal.api.ErrorCode;
import kinggora.portal.domain.Comment;
import kinggora.portal.exception.BizException;
import kinggora.portal.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public Comment findCommentById(Integer id) {
        return commentRepository.findCommentById(id)
                .orElseThrow(() -> new BizException(ErrorCode.COMMENT_NOT_FOUND));
    }

    public void updateComment(Comment comment) {
        commentRepository.updateComment(comment);
    }

    public List<Comment> findComments(Integer boardId, Integer postId) {
        return commentRepository.findComments(boardId, postId);
    }

    public void deleteComment(Integer id) {
        if(commentRepository.childExists(id)) {
            commentRepository.hideComment(id);
        } else {
            commentRepository.deleteComment(id);
        }
    }
}
