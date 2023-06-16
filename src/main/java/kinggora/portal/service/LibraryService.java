package kinggora.portal.service;

import kinggora.portal.api.ErrorCode;
import kinggora.portal.domain.LibraryPost;
import kinggora.portal.exception.BizException;
import kinggora.portal.repository.LibraryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LibraryService {
    private final LibraryRepository libraryRepository;

    /**
     * 게시글 저장
     * @param post
     * @return 게시글 id
     */
    public int savePost(LibraryPost post) {
        return libraryRepository.savePost(post);
    }

    /**
     * 게시글 수정
     * @param post 수정할 데이터
     */
    public void updatePost(LibraryPost post) {
        libraryRepository.updatePost(post);
    }

    /**
     * 게시글 삭제
     * @param id 게시글 id
     */
    public void deletePost(Integer id) {
        libraryRepository.deletePost(id);
    }

    /**
     * 게시글 단건 조회
     * @param id 게시글 id
     * @return 게시글 정보
     */
    public LibraryPost findPostById(Integer id) {
        return libraryRepository.findPostById(id)
                .orElseThrow(() -> new BizException(ErrorCode.POST_NOT_FOUND));
    }

    /**
     * 게시글 조회수 1 증가
     * @param id 게시글 id
     */
    public void hitUp(Integer id) {
        libraryRepository.hitUp(id);
    }
}
