package kinggora.portal.mapper;

import kinggora.portal.domain.BoardInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

/**
 * MyBatis Mapper Interface
 * BoardInfoMapper.xml에 정의된 SQL과 메서드를 매핑
 */
@Mapper
public interface BoardInfoMapper {

    Optional<BoardInfo> findById(int id);

    List<BoardInfo> findBoardInfos();

    Optional<BoardInfo> findByPostId(int postId);
}
