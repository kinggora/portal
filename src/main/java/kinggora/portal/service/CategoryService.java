package kinggora.portal.service;

import kinggora.portal.domain.Category;
import kinggora.portal.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * 게시판 별 카테고리 조회
     * @param id 게시판 id
     * @return 카테고리 정보
     */
    public List<Category> findCategories(Integer id) {
        return categoryRepository.findCategories(id);
    }
}
