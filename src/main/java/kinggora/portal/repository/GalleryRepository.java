package kinggora.portal.repository;

import kinggora.portal.domain.GalleryPost;
import kinggora.portal.mapper.GalleryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Slf4j
@Repository
@RequiredArgsConstructor
public class GalleryRepository {
    private final GalleryMapper mapper;

    /**
     * 게시글 저장
     * @param post
     * @return 게시글 id
     */
    public int savePost(GalleryPost post) {
        if(mapper.savePost(post) != 1){
            log.error("fail GalleryRepository.savePost");
        }
        return post.getId();
    }

    /**
     * 게시글 단건 조회
     * @param id 게시글 id
     * @return 게시글 정보
     */
    public Optional<GalleryPost> findPostById(Integer id) {
        return mapper.findPostById(id);
    }

    /**
     * 게시글 조회수 1 증가
     * @param id 게시글 id
     */
    public void hitUp(Integer id) {
        if(mapper.hitUp(id) != 1) {
            log.error("fail GalleryRepository.hitUp");
        }
    }

    /**
     * 게시글 수정
     * @param post 수정할 데이터
     */
    public void updatePost(GalleryPost post) {
        if(mapper.updatePost(post) != 1) {
            log.error("fail GalleryRepository.updatePost");
        }
    }

    /**
     * 게시글 삭제
     * @param id 게시글 id
     */
    public void deletePost(Integer id) {
        if(mapper.deletePost(id) != 1) {
            log.error("fail GalleryRepository.deletePost");
        }
    }
}
