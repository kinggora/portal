package kinggora.portal.mapper;

import kinggora.portal.domain.Pageable;
import kinggora.portal.domain.Post;
import kinggora.portal.model.request.SearchCriteria;
import kinggora.portal.model.response.BoardDetail;
import kinggora.portal.model.response.CommonBoardItem;
import kinggora.portal.model.response.QnaBoardItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

/**
 * MyBatis Mapper Interface
 * BoardMapper.xml에 정의된 SQL과 메서드를 매핑
 */
@Mapper
public interface BoardMapper {

    int save(Post post);

    Optional<Post> findById(int id);

    Optional<Post> findByFileId(int fileId);

    int update(Post post);

    int deleteById(int id);

    Optional<BoardDetail> findBoardDetail(int id);

    int hitUp(int id);

    List<CommonBoardItem> findCommonBoardItems(@Param("pageable") Pageable pageable, @Param("criteria") SearchCriteria searchCriteria);

    List<QnaBoardItem> findQnaBoardItems(@Param("pageable") Pageable pageable, @Param("criteria") SearchCriteria searchCriteria);

    int findPostsCount(SearchCriteria sc);

    boolean existsById(int id);

    boolean hasChild(int parentId);

    OptionalInt findChild(int parentId);
}
