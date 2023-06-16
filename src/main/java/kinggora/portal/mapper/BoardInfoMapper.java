package kinggora.portal.mapper;

import kinggora.portal.domain.BoardInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface BoardInfoMapper {

    Optional<BoardInfo> findBoardInfoById(Integer id);
    List<BoardInfo> findBoardInfo();
    Integer saveBoardInfo(BoardInfo boardInfo);
}
