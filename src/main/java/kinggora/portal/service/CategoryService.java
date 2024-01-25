package kinggora.portal.service;

import kinggora.portal.domain.Category;
import kinggora.portal.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 카테고리 서비스
 */
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * 게시판 별 카테고리 조회
     *
     * @param id 게시판 id
     * @return 카테고리 정보
     */
    public List<Category> findCategories(Integer id) {
        return categoryRepository.findByBoardId(id);
    }

    /**
     * 특정 게시판에 id가 categoryId인 카테고리가 존재하는지 확인
     *
     * @param categoryId 카테고리 id
     * @param boardId    게시판 id
     * @return 존재 여부
     */
    public boolean isCategoryOf(int categoryId, int boardId) {
        return categoryRepository.isCategoryOf(categoryId, boardId);
    }
}
