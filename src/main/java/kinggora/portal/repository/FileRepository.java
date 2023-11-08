package kinggora.portal.repository;

import kinggora.portal.domain.UploadFile;
import kinggora.portal.domain.type.FileType;
import kinggora.portal.mapper.FileMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FileRepository {

    private final FileMapper mapper;

    /**
     * DB에 첨부 파일 정보 저장
     *
     * @param files 파일 정보 리스트
     */
    public void saveFiles(List<UploadFile> files) {
        if (mapper.saveFiles(files) != 1) {
            log.error("fail FreeRepository.savePost");
        }
    }

    /**
     * 파일 정보 단건 조회
     *
     * @param id 파일 id
     * @return 파일 정보
     */
    public Optional<UploadFile> findFileById(Integer id) {
        return mapper.findFileById(id);
    }


    /**
     * 해당 게시글에 첨부된 파일 정보 조회
     *
     * @param postId 게시글 id
     * @return 파일 정보 리스트
     */
    public List<UploadFile> findFiles(Integer postId) {
        return mapper.findFiles(postId);
    }

    public List<UploadFile> findFilesOfType(List<Integer> postIds, FileType type) {
        return mapper.findFilesOfType(postIds, type);
    }


    /**
     * 파일 삭제
     *
     * @param id 파일 id
     */
    public void deleteFile(Integer id) {
        if (mapper.deleteFiles(id) != 1) {
            log.error("fail FileRepository.deleteFile");
        }
    }

    /**
     * 해당 게시글에 첨부된 파일 삭제
     *
     * @param postId 게시글 id
     */
    public void deleteFiles(Integer postId) {
        if (mapper.deleteFiles(postId) != 1) {
            log.error("fail FileRepository.deleteFilesByPostId");
        }
    }
}
