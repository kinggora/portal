package kinggora.portal.repository;

import kinggora.portal.domain.UploadFile;
import kinggora.portal.exception.BizException;
import kinggora.portal.exception.ErrorCode;
import kinggora.portal.mapper.FileMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 파일 리포지토리
 * file 테이블에 대한 CRUD 수행
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class FileRepository {

    private final FileMapper mapper;

    /**
     * 파일 정보 저장
     *
     * @param files 저장할 파일 정보
     */
    public void saveFiles(List<UploadFile> files) {
        if (mapper.saveFiles(files) == 0) {
            log.error("fail FileRepository.saveFiles");
            throw new BizException(ErrorCode.DB_ERROR, "파일 저장 실패");
        }
    }

    /**
     * 파일 정보 단건 조회
     *
     * @param id 파일 id
     * @return 파일 정보
     */
    public Optional<UploadFile> findById(int id) {
        return mapper.findById(id);
    }

    /**
     * 게시글에 첨부한 파일 정보 조회
     *
     * @param postId 게시글 id
     * @return 파일 정보 리스트
     */
    public List<UploadFile> findByPostId(int postId) {
        return mapper.findByPostId(postId);
    }

    /**
     * 파일 정보 삭제
     * update deleted=true
     *
     * @param id 파일 id
     */
    public void deleteById(int id) {
        if (mapper.deleteById(id) == 0) {
            log.error("fail FileRepository.deleteById");
            throw new BizException(ErrorCode.DB_ERROR, "파일 삭제 실패");
        }
    }

    /**
     * 게시글에 첨부한 파일 정보 삭제
     *
     * @param postId 게시글 id
     */
    public void deleteByPostId(int postId) {
        if (mapper.deleteByPostId(postId) == 0) {
            log.error("fail FileRepository.deleteByPostId");
            throw new BizException(ErrorCode.DB_ERROR, "게시글 파일 삭제 실패");
        }
    }
}
