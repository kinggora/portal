package kinggora.portal.service;

import kinggora.portal.api.ErrorCode;
import kinggora.portal.domain.CommonPost;
import kinggora.portal.domain.dto.PageInfo;
import kinggora.portal.domain.dto.SearchCriteria;
import kinggora.portal.exception.BizException;
import kinggora.portal.repository.CommonPostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonPostService {

    private final CommonPostRepository commonPostRepository;

    /**
     * 게시글 저장
     *
     * @param post
     * @return 게시글 id
     */
    public int savePost(CommonPost post) {
        return commonPostRepository.savePost(post);
    }

    /**
     * 게시글 수정
     *
     * @param post 수정할 데이터
     */
    public void updatePost(CommonPost post) {
        commonPostRepository.updatePost(post);
    }

    /**
     * 게시글 삭제
     *
     * @param id 게시글 id
     */
    public void deletePost(Integer id) {
        commonPostRepository.deletePost(id);
    }

    /**
     * 게시글 단건 조회
     *
     * @param id 게시글 id
     * @return 게시글 정보
     */
    public CommonPost findPostById(Integer id) {
        return commonPostRepository.findPostById(id)
                .orElseThrow(() -> new BizException(ErrorCode.POST_NOT_FOUND));
    }

    /**
     * 검색 조건에 해당하는 게시글 조회 + 페이징 처리
     *
     * @param criteria 검색 조건
     * @return 게시글 리스트
     */
    public List<CommonPost> findPosts(SearchCriteria criteria) {
        int startRow = (criteria.getPage() - 1) * criteria.getPageSize();
        return commonPostRepository.findPosts(criteria, startRow, criteria.getPageSize());
    }

    public PageInfo getPageInfo(SearchCriteria criteria) {
        int totalCount = commonPostRepository.findPostsCount(criteria);
        int totalPages = totalCount == 0 ? 1 : (totalCount - 1) / criteria.getPageSize() + 1;
        return PageInfo.builder()
                .pageNum(criteria.getPage())
                .pageSize(criteria.getPageSize())
                .totalCount(totalCount)
                .totalPages(totalPages)
                .build();
    }

    /**
     * 게시글 조회수 1 증가
     *
     * @param id 게시글 id
     */
    public void hitUp(Integer id) {
        commonPostRepository.hitUp(id);
    }

}
