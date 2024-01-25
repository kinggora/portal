package kinggora.portal.mapper;

import kinggora.portal.domain.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * MyBatis Mapper Interface
 * CategoryMapper.xml에 정의된 SQL과 메서드를 매핑
 */
@Mapper
public interface CategoryMapper {

    List<Category> findByBoardId(Integer boardId);

    boolean isCategoryOf(@Param("id") int id, @Param("boardId") int boardId);
}
