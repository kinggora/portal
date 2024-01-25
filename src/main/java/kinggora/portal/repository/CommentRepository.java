package kinggora.portal.repository;

import kinggora.portal.controller.api.error.ErrorCode;
import kinggora.portal.domain.Comment;
import kinggora.portal.exception.BizException;
import kinggora.portal.mapper.CommentMapper;
import kinggora.portal.model.response.CommentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 댓글 리포지토리
 * comment 테이블에 대한 CRUD 수행
 */
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
    public int save(Comment comment) {
        if (mapper.save(comment) == 0) {
            log.error("fail CommentRepository.save");
            throw new BizException(ErrorCode.DB_ERROR, "댓글 등록 실패");
        }
        return comment.getId();
    }

    /**
     * 댓글 단건 조회
     *
     * @param id 댓글 id
     * @return 댓글 정보
     */
    public Optional<Comment> findById(Integer id) {
        return mapper.findById(id);
    }

    /**
     * 게시글에 대한 댓글 조회
     *
     * @param postId 게시글 id
     * @return 댓글 리스트
     */
    public List<CommentResponse> findByPostId(int postId) {
        return mapper.findByPostId(postId);
    }

    /**
     * 댓글 수정
     *
     * @param comment 수정할 데이터
     */
    public void update(Comment comment) {
        if (mapper.update(comment) == 0) {
            log.error("fail CommentRepository.update");
            throw new BizException(ErrorCode.DB_ERROR, "댓글 수정 실패");
        }
    }

    /**
     * 댓글 숨기기
     * update deleted=true
     *
     * @param id 댓글 id
     */
    public void hideById(int id) {
        if (mapper.hideById(id) == 0) {
            log.error("fail CommentRepository.hideById");
            throw new BizException(ErrorCode.DB_ERROR, "댓글 삭제 실패");
        }
    }

    /**
     * 댓글 삭제
     *
     * @param id 댓글 id
     */
    public void deleteById(int id) {
        if (mapper.deleteById(id) == 0) {
            log.error("fail CommentRepository.deleteById");
            throw new BizException(ErrorCode.DB_ERROR, "댓글 삭제 실패");
        }
    }

    /**
     * 자식 댓글의 refOrder 찾기
     * 부모 댓글보다 refOrder가 큰 댓글 중 부모와 같거나 작은 depth를 가진 댓글이 첫 번째로 나타나는 위치
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
    public int getMaxRef() {
        return mapper.getMaxRef();
    }

    /**
     * 그룹 내 refOrder 컬럼의 최댓값 반환
     *
     * @param ref 댓글 그룹
     * @return refOrder 최댓값
     */
    public int getMaxRefOrder(int ref) {
        return mapper.getMaxRefOrder(ref);
    }

    /**
     * 동일한 그룹 내 comment보다 뒷 순서인 댓글 중에서 depth가 같거나 작은 댓글이 존재하는지
     * 존재 X : 유일한 Minimum Depth => true 반환
     * 존재 : 유일하지 않거나 최솟값이 아님 => false 반환
     *
     * @param comment 댓글 정보
     */
    public boolean isOnlyMinimumDepth(Comment comment) {
        return mapper.isOnlyMinimumDepth(comment);
    }

    /**
     * 댓글 그룹 내 refOrder 이상의 컬럼 업데이트
     * refOrder = refOrder + 1
     *
     * @param ref      댓글 그룹
     * @param refOrder 업데이트 기준 값
     */
    public void refOrderUp(int ref, int refOrder) {
        if (mapper.refOrderUp(ref, refOrder) == 0) {
            log.error("fail CommentRepository.updateRefOrder");
        }
    }

    /**
     * 자식(대댓글) 존재 여부
     *
     * @param id 댓글 id
     * @return true: 대댓글 존재o, false: 대댓글 존재x
     */
    public boolean hasChild(int id) {
        return mapper.hasChild(id);
    }


    /**
     * 자식(대댓글) 개수 조회
     *
     * @param id 댓글 id
     * @return 자식(대댓글) 개수
     */
    public int getChildCount(int id) {
        return mapper.getChildCount(id);
    }

}
