package kinggora.portal.service;

import kinggora.portal.api.ErrorCode;
import kinggora.portal.domain.BoardInfo;
import kinggora.portal.exception.BizException;
import kinggora.portal.repository.BoardInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardInfoService {

    private final BoardInfoRepository boardInfoRepository;


    public List<BoardInfo> findBoardInfos() {
        return boardInfoRepository.findBoardInfos();
    }

    public BoardInfo findBoardInfoById(int id) {
        return boardInfoRepository.findBoardInfoById(id)
                .orElseThrow(() -> new BizException(ErrorCode.BOARD_NOT_FOUND));
    }

}
