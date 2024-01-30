package kinggora.portal.repository;

import kinggora.portal.domain.BoardInfo;
import kinggora.portal.mapper.BoardInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 게시판 정보 리포지토리
 * board_info 테이블에 대한 CRUD 수행
 */
@Repository
@RequiredArgsConstructor
public class BoardInfoRepository {

    private final BoardInfoMapper mapper;

    /**
     * 게시판 정보 조회
     *
     * @param id 조회할 게시판 id
     * @return 게시판 정보
     */
    public Optional<BoardInfo> findById(int id) {
        return mapper.findById(id);
    }

    /**
     * 모든 게시판 조회
     *
     * @return 모든 게시판 정보
     */
    public List<BoardInfo> findBoardInfos() {
        return mapper.findBoardInfos();
    }

    /**
     * 게시글이 등록된 게시판 조회
     *
     * @param postId 게시글 id
     * @return 게시판 정보
     */
    public Optional<BoardInfo> findByPostId(int postId) {
        return mapper.findByPostId(postId);
    }
}
