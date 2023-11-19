package kinggora.portal.repository;

import kinggora.portal.domain.CommonPost;
import kinggora.portal.domain.Post;
import kinggora.portal.domain.QnaPost;
import kinggora.portal.domain.dto.SearchCriteria;
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
     * 검색 조건에 해당하는 게시글 조회 + 페이징 처리
     *
     * @param criteria 검색 조건
     * @param startRow 페이징 (offset)
     * @param pageSize 페이징 (limit)
     * @return 게시글 리스트
     */
    public List<CommonPost> findPosts(SearchCriteria criteria, int startRow, int pageSize) {
        return mapper.findPosts(criteria, startRow, pageSize);
    }

    /**
     * 검색 조건에 해당하는 Qna 게시글 조회 + 페이징 처리
     *
     * @param criteria 검색 조건
     * @param startRow 페이징 (offset)
     * @param pageSize 페이징 (limit)
     * @return 게시글 리스트
     */
    public List<QnaPost> findQuestions(SearchCriteria criteria, int startRow, int pageSize) {
        return mapper.findQuestions(criteria, startRow, pageSize);
    }

    public List<Post> findQnA(int parentId) {
        return mapper.findQnA(parentId);
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
        if (mapper.hitUp(id) != 1) {
            log.error("fail CommonPostRepository.hitUp");
        }
    }

    /**
     * 게시글 수정
     *
     * @param post 수정할 데이터
     */
    public void updatePost(Post post) {
        if (mapper.updatePost(post) != 1) {
            log.error("fail CommonPostRepository.updatePost");
        }
    }

    /**
     * 게시글 삭제
     *
     * @param id 게시글 id
     */
    public void deletePost(int id) {
        if (mapper.deletePost(id) != 1) {
            log.error("fail CommonPostRepository.deletePost");
        }
    }

    public boolean childExists(int id) {
        return mapper.childExists(id);
    }
}
