package kinggora.portal.mapper;

import kinggora.portal.domain.BoardInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface BoardInfoMapper {

    Optional<BoardInfo> findBoardInfoById(Integer id);

    Optional<BoardInfo> findBoardInfoByName(String name);

    List<BoardInfo> findBoardInfos();

    Integer saveBoardInfo(BoardInfo boardInfo);
}
