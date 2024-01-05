package kinggora.portal.service;

import kinggora.portal.api.ErrorCode;
import kinggora.portal.domain.Comment;
import kinggora.portal.domain.dto.request.CommentDto;
import kinggora.portal.domain.dto.response.CommentResponse;
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
     * 댓글 저장 (RootComment)
     * ref: 기존에 존재하는 ref의 최댓값 + 1
     *
     * @param dto 사용자 입력 데이터
     * @return 댓글 id
     */
    public int saveRootComment(CommentDto dto) {
        int ref = commentRepository.findMaxRef() + 1;
        Comment comment = dto.toRootComment(ref);
        return commentRepository.saveComment(comment);
    }

    /**
     * 댓글 저장 (ChildComment)
     * ref: 부모와 동일한 값 (그룹)
     * depth: 부모의 depth + 1
     *
     * @param parentId 부모 id
     * @param dto      사용자 입력 데이터
     * @return 댓글 id
     */
    public int saveChildComment(int parentId, CommentDto dto) {
        Comment parent = findCommentById(parentId);
        if (parent.isDeleted()) {
            throw new BizException(ErrorCode.ALREADY_DELETED_COMMENT, "부모가 이미 삭제된 댓글입니다.");
        }
        int refOrder = findRefOrder(parent);
        Comment comment = dto.toChildComment(refOrder, parent);
        return commentRepository.saveComment(comment);
    }

    public Comment findCommentById(int id) {
        return commentRepository.findCommentById(id).orElseThrow(
                () -> new BizException(ErrorCode.COMMENT_NOT_FOUND));
    }

    public void updateComment(int id, CommentDto dto) {
        commentRepository.updateComment(dto.toUpdateComment(id));
    }

    public List<CommentResponse> findComments(int postId) {
        return commentRepository.findComments(postId);
    }

    /**
     * 댓글 삭제
     * 삭제 대상이 자식이 있으면 숨김 처리, 없으면 조상에서 함께 삭제할 댓글 탐색
     */
    public void deleteComment(int id) {
        Comment comment = findCommentById(id);
        if (comment.isDeleted()) {
            throw new BizException(ErrorCode.ALREADY_DELETED_COMMENT);
        }
        if (commentRepository.childExists(id)) {
            commentRepository.hideComment(id);
        } else {
            deleteAncestorComment(comment);
        }
    }

    /**
     * 삭제할 수 있는 댓글을 재귀적으로 반환
     * 삭제 대상의 부모가 존재하지 않는다면 comment 만 삭제
     * 부모가 다음 조건을 모두 만족 시 함께 삭제
     * 1. parent.deleted == true
     * 2. count(parent.children) == 1
     *
     * @param comment 재귀적으로 조상까지 삭제할 수 있는지 탐색하고자 하는 대상
     */
    private void deleteAncestorComment(Comment comment) {
        if (comment.getParent() != null) {
            Optional<Comment> optionalParent = commentRepository.findCommentById(comment.getParent());
            if (optionalParent.isPresent()) {
                Comment parent = optionalParent.get();
                if (parent.isDeleted() && commentRepository.findChildCount(parent.getId()) == 1) {
                    deleteAncestorComment(parent);
                }
            }
        }
        commentRepository.deleteComment(comment.getId());
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
