package kinggora.portal.service;

import kinggora.portal.controller.api.error.ErrorCode;
import kinggora.portal.domain.BoardInfo;
import kinggora.portal.exception.BizException;
import kinggora.portal.repository.BoardInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 게시판 정보 서비스
 */
@Service
@RequiredArgsConstructor
public class BoardInfoService {

    private final BoardInfoRepository boardInfoRepository;

    /**
     * 모든 게시판 정보 조회
     *
     * @return BoardInfo 리스트
     */
    public List<BoardInfo> findBoardInfos() {
        return boardInfoRepository.findAll();
    }

    /**
     * 게시판 정보 단건 조회
     *
     * @param id 게시판 id
     * @return 게시판 정보
     * @throws BizException 게시판이 존재하지 않는 경우 발생
     */
    public BoardInfo findBoardInfoById(int id) {
        return boardInfoRepository.findById(id)
                .orElseThrow(() -> new BizException(ErrorCode.BOARD_NOT_FOUND));
    }

}
