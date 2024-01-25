package kinggora.portal.repository;

import kinggora.portal.domain.Pageable;
import kinggora.portal.domain.Post;
import kinggora.portal.exception.BizException;
import kinggora.portal.exception.ErrorCode;
import kinggora.portal.mapper.BoardMapper;
import kinggora.portal.model.data.request.SearchCriteria;
import kinggora.portal.model.data.response.BoardDetail;
import kinggora.portal.model.data.response.CommonBoardItem;
import kinggora.portal.model.data.response.QnaBoardItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 게시글 리포지토리
 * boards 테이블에 대한 CRUD 수행
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class BoardRepository {

    private final BoardMapper mapper;

    /**
     * 게시글 저장
     *
     * @param post 게시글 정보
     * @return 게시글 id
     */
    public int save(Post post) {
        if (mapper.save(post) == 0) {
            log.error("fail BoardRepository.save");
            throw new BizException(ErrorCode.DB_ERROR, "게시글 등록 실패");
        }
        return post.getId();
    }

    /**
     * 게시글 단건 조회
     *
     * @param id 게시글 id
     * @return 게시글 정보
     */
    public Optional<Post> findById(int id) {
        return mapper.findById(id);
    }

    /**
     * 파일 id로 게시글 단건 조회
     *
     * @param fileId 파일 id
     * @return 게시글 정보
     */
    public Optional<Post> findByFileId(int fileId) {
        return mapper.findByFileId(fileId);
    }

    /**
     * 게시글 수정
     *
     * @param post 수정할 게시글
     */
    public void update(Post post) {
        if (mapper.update(post) == 0) {
            log.error("fail BoardRepository.update");
            throw new BizException(ErrorCode.DB_ERROR, "게시글 수정 실패");
        }
    }

    /**
     * 게시글 삭제
     * update deleted=true
     *
     * @param id 삭제할 게시글 id
     */
    public void deleteById(int id) {
        if (mapper.deleteById(id) == 0) {
            log.error("fail BoardRepository.deleteById");
            throw new BizException(ErrorCode.DB_ERROR, "게시글 삭제 실패");
        }
    }

    /**
     * 게시글 상세 조회
     *
     * @param id 게시글 id
     * @return 게시글 상세 정보
     */
    public Optional<BoardDetail> findBoardDetail(int id) {
        return mapper.findBoardDetail(id);
    }

    /**
     * 검색 조건에 해당하는 CommonBoardItem 조회 + 페이징 처리
     *
     * @param pageable       페이징 조건
     * @param searchCriteria 검색 조건
     * @return 게시글 리스트
     */
    public List<CommonBoardItem> findCommonBoardItems(Pageable pageable, SearchCriteria searchCriteria) {
        return mapper.findCommonBoardItems(pageable, searchCriteria);
    }

    /**
     * 검색 조건에 해당하는 QnaBoardItem 조회 + 페이징 처리
     *
     * @param pageable       페이징 조건
     * @param searchCriteria 검색 조건
     * @return 게시글 리스트
     */
    public List<QnaBoardItem> findQnaBoardItems(Pageable pageable, SearchCriteria searchCriteria) {
        return mapper.findQnaBoardItems(pageable, searchCriteria);
    }

    /**
     * 검색 조건에 해당하는 게시글 개수 조회
     *
     * @param criteria 검색 조건
     * @return 게시글 개수
     */
    public int findPostsCount(SearchCriteria criteria) {
        return mapper.findPostsCount(criteria);
    }

    /**
     * 게시글 존재 여부 조회
     *
     * @param id 게시글 id
     * @return 게시글 존재 여부
     */
    public boolean existsById(int id) {
        return mapper.existsById(id);
    }

    /**
     * 자식 게시글 존재 여부 조회
     *
     * @param parentId 부모 id
     * @return 자식 존재 여부
     */
    public boolean hasChild(int parentId) {
        return mapper.hasChild(parentId);
    }

    /**
     * 자식 게시글 id 조회
     * 자식이 없다면 null 반환
     *
     * @param parentId 부모 id
     * @return 자식 게시글 id or null
     */
    public Optional<Integer> findChild(int parentId) {
        return mapper.findChild(parentId);
    }

    /**
     * 게시글 조회수(hit) 증가
     * hit = hit + 1
     *
     * @param id 게시글 id
     */
    public void hitUp(int id) {
        if (mapper.hitUp(id) == 0) {
            log.error("fail BoardRepository.hitUp");
            throw new BizException(ErrorCode.DB_ERROR, "게시글 조회수 증가 실패");
        }
    }

}
