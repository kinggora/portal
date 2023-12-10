package kinggora.portal.mapper;

import kinggora.portal.domain.BoardInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface BoardInfoMapper {

    Optional<BoardInfo> findBoardInfoById(int id);

    Optional<BoardInfo> findBoardInfoByName(String name);

    Optional<BoardInfo> findBoardInfoByPostId(int postId);

    List<BoardInfo> findBoardInfos();

    Integer saveBoardInfo(BoardInfo boardInfo);
}
