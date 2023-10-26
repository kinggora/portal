package kinggora.portal.repository;

import kinggora.portal.domain.Comment;
import kinggora.portal.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CommentRepository {

    private final CommentMapper mapper;

    /**
     * 댓글 저장
     *
     * @param comment 댓글 정보
     * @return 댓글 id
     */
    public int saveComment(Comment comment) {
        if (mapper.saveComment(comment) != 1) {
            log.error("fail CommentRepository.saveComment");
        }
        return comment.getId();
    }

    /**
     * 자식 댓글의 refOrder 찾기
     * refOrder상 부모 댓글 이후로 부모와 같거나 작은 depth 값을 가진 댓글이 첫 번째로 나타나는 위치
     *
     * @param parent 부모 댓글
     * @return 자식 댓글의 refOrder
     */
    public int findRefOrderOfChild(Comment parent) {
        return mapper.findRefOrderOfChild(parent);
    }

    /**
     * ref 컬럼의 최댓값을 반환
     *
     * @return Max(ref)
     */
    public int findMaxRef() {
        return mapper.findMaxRef();
    }


    /**
     * 그룹 내 refOrder 컬럼의 최댓값 반환
     *
     * @param ref 댓글 그룹
     * @return refOrder 최댓값
     */
    public int findMaxRefOrder(int ref) {
        return mapper.findMaxRefOrder(ref);
    }

    /**
     * 동일한 그룹 내 해당 댓글보다 뒷 순서인 댓글 중에서 depth가 같거나 작은 댓글이 존재하는지
     * 존재 : 유일하지 않거나 최솟값이 아님 => false 반환
     * 존재 X : 유일한 Minimum Depth => true 반환
     *
     * @param comment 댓글 정보
     * @return
     */
    public boolean isOnlyMinimumDepth(Comment comment) {
        return mapper.isOnlyMinimumDepth(comment);
    }

    /**
     * refOrder 이상의 컬럼 대상 업데이트
     * refOrder = refOrder + 1
     *
     * @param ref      댓글 그룹
     * @param refOrder 업데이트 기준 값
     * @return 수정된 row 개수
     */
    public int updateRefOrder(Integer ref, Integer refOrder) {
        return mapper.updateRefOrder(ref, refOrder);
    }

    /**
     * 댓글 단건 조회
     *
     * @param id 댓글 id
     * @return 댓글 정보
     */
    public Optional<Comment> findCommentById(Integer id) {
        return mapper.findCommentById(id);
    }

    /**
     * 게시글에 대한 댓글 조회
     *
     * @param postId 게시글 id
     * @return 댓글 리스트
     */
    public List<Comment> findComments(int postId) {
        return mapper.findComments(postId);
    }

    /**
     * 댓글 수정
     *
     * @param comment 수정할 데이터
     */
    public void updateComment(Comment comment) {
        if (mapper.updateComment(comment) != 1) {
            log.error("fail CommentRepository.updateComment");
        }
    }

    /**
     * 댓글 숨기기 (del_flag=true)
     *
     * @param id 댓글 id
     */
    public void hideCommentById(Integer id) {
        if (mapper.hideCommentById(id) != 1) {
            log.error("fail CommentRepository.hideCommentById");
        }
    }

    /**
     * 해당 게시글에 대한 댓글 숨기기
     *
     * @param postId 게시글 id
     */
    public void hideComments(Integer postId) {
        mapper.hideComments(postId);
    }

    /**
     * 댓글 삭제 (delete)
     *
     * @param id 댓글 id
     */
    public void deleteComment(Integer id) {
        mapper.deleteComment(id);
    }

    /**
     * 댓글에 대한 자식(대댓글) 존재 여부
     *
     * @param id 댓글 id
     * @return true: 대댓글 존재o, false: 대댓글 존재x
     */
    public boolean childExists(Integer id) {
        return mapper.childExists(id);
    }
}
