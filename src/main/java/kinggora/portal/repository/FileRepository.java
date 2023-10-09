package kinggora.portal.repository;

import kinggora.portal.domain.AttachFile;
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
    public void saveFile(List<AttachFile> files) {
        if (mapper.saveFile(files) != 1) {
            log.error("fail FreeRepository.savePost");
        }
    }

    /**
     * 파일 정보 단건 조회
     *
     * @param id 파일 id
     * @return 파일 정보
     */
    public Optional<AttachFile> findFileById(Integer id) {
        return mapper.findFileById(id);
    }


    /**
     * 해당 게시글에 첨부된 파일 정보 조회
     *
     * @param boardId 게시판 id
     * @param postId  게시글 id
     * @return 파일 정보 리스트
     */
    public List<AttachFile> findFiles(Integer boardId, Integer postId) {
        return mapper.findFiles(boardId, postId);
    }

    /**
     * 해당 게시글에 첨부된 파일 삭제
     *
     * @param boardId 게시판 id
     * @param postId  게시글 id
     */
    public void deleteFiles(Integer boardId, Integer postId) {
        if (mapper.deleteFiles(boardId, postId) != 1) {
            log.error("fail FileRepository.deleteFiles");
        }
    }
}
