package kinggora.portal.mapper;

import kinggora.portal.domain.GalleryPost;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface GalleryMapper {

    /**
     * 게시글 저장
     * @param post 게시글 정보
     * @return 게시글 id
     */
    int savePost(GalleryPost post);

    /**
     * 게시글 단건 조회
     * @param id 게시글 id
     * @return 게시글 정보
     */
    Optional<GalleryPost> findPostById(Integer id);

    /**
     * 게시글 조회수 1 증가
     * @param id 게시글 id
     */
    int hitUp(Integer id);

    /**
     * 게시글 수정
     * @param post 수정할 데이터
     * @return 수정된 게시글 개수
     */
    int updatePost(GalleryPost post);

    /**
     * 게시글 삭제
     * @param id 게시글 id
     * @return 삭제된 게시글 개수
     */
    int deletePost(Integer id);
}
