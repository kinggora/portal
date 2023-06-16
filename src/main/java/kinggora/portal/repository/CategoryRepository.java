package kinggora.portal.repository;

import kinggora.portal.domain.Category;
import kinggora.portal.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
@RequiredArgsConstructor
public class CategoryRepository {

    private final CategoryMapper mapper;

    /**
     * 게시판 id에 해당하는 카테고리 반환
     * @param boardId 게시판 id
     * @return
     */
    public ArrayList<Category> findCategories(Integer boardId) {
        return mapper.findCategories(boardId);
    }
}
