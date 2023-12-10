package kinggora.portal.repository;

import kinggora.portal.api.ErrorCode;
import kinggora.portal.domain.CommonPost;
import kinggora.portal.domain.Post;
import kinggora.portal.domain.QnaPost;
import kinggora.portal.domain.dto.request.SearchCriteria;
import kinggora.portal.exception.BizException;
import kinggora.portal.mapper.BoardsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BoardsRepository {

    private final BoardsMapper mapper;

    /**
     * 게시글 저장
     *
     * @param post 게시글 정보
     * @return 게시글 id
     */
    public int savePost(Post post) {
        if (mapper.savePost(post) != 1) {
            log.error("fail CommonPostRepository.savePost");
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
    public Optional<Post> findPostById(Integer id) {
        return mapper.findPostById(id);
    }

    /**
     * 파일 id로 게시글 단건 조회
     *
     * @param fileId 파일 id
     * @return 게시글 정보
     */
    public Optional<Post> findPostByFileId(Integer fileId) {
        return mapper.findPostByFileId(fileId);
    }

    /**
     * 검색 조건에 해당하는 게시글 조회 + 페이징 처리
     *
     * @param criteria 검색 조건
     * @param startRow 페이징 (offset)
     * @param pageSize 페이징 (limit)
     * @return 게시글 리스트
     */
    public List<CommonPost> findCommonPosts(SearchCriteria criteria, int startRow, int pageSize) {
        return mapper.findCommonPosts(criteria, startRow, pageSize);
    }

    /**
     * 검색 조건에 해당하는 Qna 게시글 조회 + 페이징 처리
     *
     * @param criteria 검색 조건
     * @param startRow 페이징 (offset)
     * @param pageSize 페이징 (limit)
     * @return 게시글 리스트
     */
    public List<QnaPost> findQnaPosts(SearchCriteria criteria, int startRow, int pageSize) {
        return mapper.findQnaPosts(criteria, startRow, pageSize);
    }

    /**
     * 자식 게시물 조회
     * parent=parentId
     *
     * @param parentId
     * @return
     */
    public List<Post> findChildPosts(int parentId) {
        return mapper.findChildPosts(parentId);
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
     * 게시글 조회수 1 증가
     *
     * @param id 게시글 id
     */
    public void hitUp(Integer id) {
        if (mapper.hitUp(id) == 0) {
            log.error("fail CommonPostRepository.hitUp");
            throw new BizException(ErrorCode.DB_ERROR, "게시글 조회수 증가 실패");
        }
    }

    /**
     * 게시글 수정
     *
     * @param post 수정할 데이터
     */
    public void updatePost(Post post) {
        if (mapper.updatePost(post) == 0) {
            log.error("fail CommonPostRepository.updatePost");
            throw new BizException(ErrorCode.DB_ERROR, "게시글 수정 실패");
        }
    }

    /**
     * 게시글 삭제
     *
     * @param id 게시글 id
     */
    public void deletePostById(int id) {
        if (mapper.deletePostById(id) == 0) {
            log.error("fail CommonPostRepository.deletePost");
            throw new BizException(ErrorCode.DB_ERROR, "게시글 삭제 실패");
        }
    }

    public boolean childExists(int id) {
        return mapper.childExists(id);
    }

    public boolean existById(int id) {
        return mapper.existById(id);
    }
}
