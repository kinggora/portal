package kinggora.portal.repository;

import kinggora.portal.domain.FreePost;
import kinggora.portal.domain.dto.PostDto;
import kinggora.portal.domain.dto.SearchCriteria;
import kinggora.portal.mapper.FreeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FreeRepository {

    private final FreeMapper mapper;

    /**
     *
     * @param post
     * @return
     */
    public int savePost(PostDto post) {
       if(mapper.savePost(post) != 1){
           log.error("fail FreeRepository.savePost");
       }
        return post.getId();
    }

    public Optional<FreePost> findPostById(Integer id) {
        return mapper.findPostById(id);
    }

    /**
     * 검색 조건에 해당하는 게시글 조회 + 페이징 처리
     * @param criteria 검색 조건
     * @param startRow 페이징 (offset)
     * @param pageSize 페이징 (limit)
     * @return 게시글 리스트
     */
    public List<FreePost> findPosts(SearchCriteria criteria, int startRow, int pageSize) {
        return mapper.findPosts(criteria, startRow, pageSize);
    }

    /**
     * 검색 조건에 해당하는 게시글 개수 조회
     * @param criteria 검색 조건
     * @return 게시글 개수
     */
    public int getPostCount(SearchCriteria criteria) {
        return mapper.getPostCount(criteria);
    }

    /**
     * 게시글 조회수 1 증가
     * @param id 게시글 id
     */
    public void hitUp(Integer id) {
        if(mapper.hitUp(id) != 1) {
            log.error("fail FreeRepository.hitUp");
        }
    }

    /**
     * 게시글 수정
     * @param post 수정할 데이터
     * @return 수정된 게시글 개수
     */
    public void updatePost(PostDto post) {
        if(mapper.updatePost(post) != 1) {
            log.error("fail FreeRepository.updatePost");
        }
    }

    /**
     * 게시글 삭제
     * @param id 게시글 id
     * @return 삭제된 게시글 개수
     */
    public void deletePost(Integer id) {
        if(mapper.deletePost(id) != 1) {
            log.error("fail FreeRepository.deletePost");
        }
    }
}
