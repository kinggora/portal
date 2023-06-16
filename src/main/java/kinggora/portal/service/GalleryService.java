package kinggora.portal.service;

import kinggora.portal.api.ErrorCode;
import kinggora.portal.domain.GalleryPost;
import kinggora.portal.exception.BizException;
import kinggora.portal.repository.GalleryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GalleryService {

    private final GalleryRepository galleryRepository;

    /**
     * 게시글 저장
     * @param post
     * @return 게시글 id
     */
    public int savePost(GalleryPost post) {
        return galleryRepository.savePost(post);
    }

    /**
     * 게시글 수정
     * @param post 수정할 데이터
     */
    public void updatePost(GalleryPost post) {
        galleryRepository.updatePost(post);
    }

    /**
     * 게시글 삭제
     * @param id 게시글 id
     */
    public void deletePost(Integer id) {
        galleryRepository.deletePost(id);
    }

    /**
     * 게시글 단건 조회
     * @param id 게시글 id
     * @return 게시글 정보
     */
    public GalleryPost findPostById(Integer id) {
        return galleryRepository.findPostById(id)
                .orElseThrow(() -> new BizException(ErrorCode.POST_NOT_FOUND));
    }

    /**
     * 게시글 조회수 1 증가
     * @param id 게시글 id
     */
    public void hitUp(Integer id) {
        galleryRepository.hitUp(id);
    }

}
