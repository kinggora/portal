package kinggora.portal.service;

import kinggora.portal.api.ErrorCode;
import kinggora.portal.domain.BoardInfo;
import kinggora.portal.exception.BizException;
import kinggora.portal.repository.BoardInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardInfoService {

    private final BoardInfoRepository boardInfoRepository;


    public List<BoardInfo> findBoardInfo() {
        return boardInfoRepository.findBoardInfo();
    }

    public BoardInfo findBoardInfoById(Integer id) {
        return boardInfoRepository.findBoardInfoById(id)
                .orElseThrow(() -> new BizException(ErrorCode.BOARD_NOT_FOUND));
    }
}
