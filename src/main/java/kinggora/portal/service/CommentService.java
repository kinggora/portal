package kinggora.portal.service;

import kinggora.portal.controller.api.error.ErrorCode;
import kinggora.portal.domain.Comment;
import kinggora.portal.exception.BizException;
import kinggora.portal.model.request.CommentDto;
import kinggora.portal.model.response.CommentResponse;
import kinggora.portal.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 댓글 서비스
 * 댓글 등록, 수정, 삭제 등 댓글 관련 서비스
 * properties 파일로부터 댓글 최대 깊이 설정 초기화
 */
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    @Value("${comment.maximum-depth}")
    private int MAXIMUM_DEPTH;

    /**
     * 댓글 저장 (RootComment)
     * ref : 기존에 존재하는 ref의 최댓값 + 1
     *
     * @param dto 사용자 입력 데이터
     * @return 댓글 id
     */
    public int saveRootComment(CommentDto dto) {
        int ref = commentRepository.getMaxRef() + 1;
        Comment comment = dto.toRootComment(ref);
        return commentRepository.save(comment);
    }

    /**
     * 댓글 저장 (ChildComment)
     * 부모가 이미 삭제(숨김)된 댓글이거나 maximum depth 이면 예외 발생
     * 부모 댓글 기반으로 도메인 생성
     *
     * @param parentId 부모 id
     * @param dto      사용자 입력 데이터
     * @return 댓글 id
     */
    public int saveChildComment(int parentId, CommentDto dto) {
        Comment parent = findCommentById(parentId);
        if (parent.isDeleted()) {
            throw new BizException(ErrorCode.ALREADY_DELETED_COMMENT);
        } else if (parent.getDepth() == MAXIMUM_DEPTH) {
            throw new BizException(ErrorCode.OVER_MAXIMUM_COMMENT_DEPTH);
        }
        int refOrder = findRefOrder(parent);
        Comment comment = dto.toChildComment(refOrder, parent);
        return commentRepository.save(comment);
    }

    /**
     * 댓글 단건 조회
     *
     * @param id 댓글 id
     * @return 조회한 댓글
     * @throws BizException 댓글이 존재하지 않거나 이미 삭제된 댓글일 경우 발생
     */
    public Comment findCommentById(int id) {
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new BizException(ErrorCode.COMMENT_NOT_FOUND));
        if (comment.isDeleted()) {
            throw new BizException(ErrorCode.ALREADY_DELETED_COMMENT);
        }
        return comment;
    }

    /**
     * 댓글 수정
     *
     * @param id  수정할 댓글 id
     * @param dto 수정 데이터
     */
    public void updateComment(int id, CommentDto dto) {
        commentRepository.update(dto.toUpdateComment(id));
    }

    /**
     * 게시글에 대한 댓글 조회
     *
     * @param postId 게시글 id
     * @return 댓글 리스트
     */
    public List<CommentResponse> findComments(int postId) {
        return commentRepository.findByPostId(postId);
    }

    /**
     * 댓글 삭제
     * 삭제 대상이 자식이 있으면 숨김 처리, 없으면 조상에서 함께 삭제할 댓글 탐색
     *
     * @param id 삭제할 댓글 id
     */
    public void deleteComment(int id) {
        Comment comment = findCommentById(id);
        if (commentRepository.hasChild(id)) {
            commentRepository.hideById(id);
        } else {
            deleteAncestorComment(comment);
        }
    }

    /**
     * 실제 삭제 수행
     * 삭제할 댓글을 파라미터로 재귀 호출
     * 삭제 대상의 부모가 존재하지 않거나 아래 조건을 만족하지 않는다면 comment 만 삭제
     * 부모가 다음 조건을 모두 만족 시 함께 삭제
     * 1. parent.deleted == true (숨김 처리됨)
     * 2. count(parent.children) == 1 (자식이 삭제하려는 댓글 뿐)
     *
     * @param comment 조상까지 삭제할 수 있는지 탐색하고자 하는 대상
     */
    private void deleteAncestorComment(Comment comment) {
        if (comment.getParent() != null) {
            Optional<Comment> optionalParent = commentRepository.findById(comment.getParent());
            if (optionalParent.isPresent()) {
                Comment parent = optionalParent.get();
                if (parent.isDeleted() && commentRepository.getChildCount(parent.getId()) == 1) {
                    deleteAncestorComment(parent);
                }
            }
        }
        commentRepository.deleteById(comment.getId());
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
            refOrder = commentRepository.getMaxRefOrder(parent.getRef()) + 1;
        } else {
            refOrder = commentRepository.findRefOrderOfChild(parent);
            commentRepository.refOrderUp(parent.getRef(), refOrder);
        }
        return refOrder;
    }
}
