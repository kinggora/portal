package kinggora.portal.repository;

import kinggora.portal.domain.FreePost;
import kinggora.portal.domain.dto.PostDto;
import kinggora.portal.mapper.FreeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FreeRepository {

    private final FreeMapper mapper;

    public int savePost(PostDto post) {
        return mapper.savePost(post);
    }

    public Optional<FreePost> findPostById(Integer id) {
        return mapper.findPostById(id);
    }

    /**
     * 게시글 조회수 1 증가
     * @param id 게시글 id
     */
    public void hitUp(Integer id) {
        mapper.hitUp(id);
    }

    /**
     * 게시글 수정
     * @param post 수정할 데이터
     * @return 수정된 게시글 개수
     */
    public int updatePost(PostDto post) {
        return mapper.updatePost(post);
    }

    /**
     * 게시글 삭제
     * @param id 게시글 id
     * @return 삭제된 게시글 개수
     */
    public int deletePost(Integer id) {
        return mapper.deletePost(id);
    }


}
