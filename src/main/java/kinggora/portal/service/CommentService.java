package kinggora.portal.service;

import kinggora.portal.api.ErrorCode;
import kinggora.portal.domain.Comment;
import kinggora.portal.domain.dto.CommentDto;
import kinggora.portal.exception.BizException;
import kinggora.portal.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    /**
     * 댓글 저장
     * parent == null => RootComment
     * parent != null => ChildComment
     * <p>
     * RootComment
     * ref: 기존에 존재하는 ref의 최댓값 + 1
     * <p>
     * ChildComment
     * ref: 부모와 동일한 값 (그룹)
     * depth: 부모의 depth + 1
     *
     * @param dto 사용자 입력 데이터
     * @return 댓글 id
     */
    public int saveComment(CommentDto dto) {
        Comment comment;
        if (dto.getParent() == null) {
            int ref = commentRepository.findMaxRef() + 1;
            comment = dto.toRootComment(ref);
        } else {
            Comment parent = findCommentById(dto.getParent());
            int refOrder = findRefOrder(parent);
            comment = dto.toChildComment(parent.getDepth() + 1, parent.getRef(), refOrder);
        }
        return commentRepository.saveComment(comment);
    }

    public Comment findCommentById(Integer id) {
        Optional<Comment> optional = commentRepository.findCommentById(id);
        if (optional.isEmpty()) {
            throw new BizException(ErrorCode.COMMENT_NOT_FOUND);
        }
        Comment comment = optional.get();
        if (comment.isDeleted()) {
            throw new BizException(ErrorCode.ALREADY_DELETED_COMMENT);
        }
        return comment;
    }

    public void updateComment(CommentDto dto) {
        commentRepository.updateComment(dto.toComment());
    }

    public List<Comment> findComments(Integer postId) {
        return commentRepository.findComments(postId);
    }

    /**
     * 삭제 대상이 자식이 있으면 숨김 처리, 없으면 삭제
     *
     * @param id
     */
    public void deleteComment(Integer id) {
        if (commentRepository.childExists(id)) {
            commentRepository.hideComment(id);
        } else {
            commentRepository.deleteComment(id);
        }
    }

    /**
     * 부모가 존재하는 댓글의 순서(refOrder) 찾기
     * 부모 댓글 이후로
     * 부모의 depth 이하 댓글이 존재하지 않으면, refOrder = refOrder 최댓값 + 1 (ref 내 마지막 위치)
     * 존재하면, refOrder = 부모의 depth 이하 댓글이 등장하는 첫 번째 위치 + refOrder 재배열
     *
     * @param parent 부모 댓글
     * @return 자식 댓글의 refOrder
     */
    private int findRefOrder(Comment parent) {
        int refOrder;
        if (commentRepository.isOnlyMinimumDepth(parent)) {
            refOrder = commentRepository.findMaxRefOrder(parent.getRef()) + 1;
        } else {
            refOrder = commentRepository.findRefOrderOfChild(parent);
            commentRepository.updateRefOrder(parent.getRef(), refOrder);
        }
        return refOrder;
    }

}
