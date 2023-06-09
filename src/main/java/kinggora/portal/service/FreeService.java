package kinggora.portal.service;

import kinggora.portal.api.ErrorCode;
import kinggora.portal.domain.BoardInfo;
import kinggora.portal.domain.Category;
import kinggora.portal.domain.FreePost;
import kinggora.portal.domain.dto.SearchCriteria;
import kinggora.portal.exception.BizException;
import kinggora.portal.repository.CategoryRepository;
import kinggora.portal.repository.FreeRepository;
import kinggora.portal.util.PageManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FreeService {

    private final FreeRepository freeRepository;
    private final CategoryRepository categoryRepository;

    /**
     * 게시글 저장
     * @param post
     * @return 게시글 id
     */
    public int savePost(FreePost post) {
        return freeRepository.savePost(post);
    }

    /**
     * 게시글 수정
     * @param post 수정할 데이터
     */
    public void updatePost(FreePost post) {
        freeRepository.updatePost(post);
    }

    /**
     * 게시글 삭제
     * @param id 게시글 id
     */
    public void deletePost(Integer id) {
        freeRepository.deletePost(id);
    }

    /**
     * 게시글 단건 조회
     * @param id 게시글 id
     * @return 게시글 정보
     */
    public FreePost findPostById(Integer id) {
        return freeRepository.findPostById(id)
                .orElseThrow(() -> new BizException(ErrorCode.POST_NOT_FOUND));
    }

    /**
     * 검색 조건에 해당하는 게시글 조회 + 페이징 처리
     * @param criteria 검색 조건
     * @return 게시글 리스트
     */
    public List<FreePost> findPosts(SearchCriteria criteria) {
        int startRow = (criteria.getPage() - 1) * PageManager.PAGE_SIZE;
        return freeRepository.findPosts(criteria, startRow, PageManager.PAGE_SIZE);
    }

    /**
     * 게시글 조회수 1 증가
     * @param id 게시글 id
     */
    public void hitUp(Integer id) {
        freeRepository.hitUp(id);
    }

}
