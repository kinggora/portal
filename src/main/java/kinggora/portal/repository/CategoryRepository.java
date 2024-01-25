package kinggora.portal.repository;

import kinggora.portal.domain.Category;
import kinggora.portal.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 카테고리 리포지토리
 * category 테이블에 대한 CRUD 수행
 */
@Repository
@RequiredArgsConstructor
public class CategoryRepository {

    private final CategoryMapper mapper;

    /**
     * boardId로 카테고리 조회
     *
     * @param boardId 게시판 id
     * @return 카테고리 리스트
     */
    public List<Category> findByBoardId(int boardId) {
        return mapper.findByBoardId(boardId);
    }

    /**
     * id와 boardId로 카테고리 존재 여부 확인
     *
     * @param id      카테고리 id
     * @param boardId 게시판 id
     * @return 카테고리 존재 여부
     */
    public boolean isCategoryOf(int id, int boardId) {
        return mapper.isCategoryOf(id, boardId);
    }
}
