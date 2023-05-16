package kinggora.portal.mapper;

import kinggora.portal.domain.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface CategoryMapper {

    /**
     * 카테고리 조회
     * @return 카테고리 리스트
     */
    ArrayList<Category> findCategories(Integer boardId);
}
