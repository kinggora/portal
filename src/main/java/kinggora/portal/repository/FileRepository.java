package kinggora.portal.repository;

import kinggora.portal.domain.AttachFile;
import kinggora.portal.mapper.FileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FileRepository {

    private final FileMapper mapper;

    /**
     * DB에 첨부 파일 정보 저장
     * @param files 파일 정보 리스트
     */
    public void saveFile(List<AttachFile> files) {
        mapper.saveFile(files);
    }

    /**
     * 파일 정보 단건 조회
     * @param id 파일 id
     * @return 파일 정보
     */
    public Optional<AttachFile> findFileById(Integer id) {
        return mapper.findFileById(id);
    }

    /**
     * 해당 게시글에 첨부된 파일 정보 조회
     * @param boardId 게시판 id
     * @param postId 게시글 id
     * @return 파일 정보 리스트
     */
    public List<AttachFile> findFiles(Integer boardId, Integer postId) {
        return mapper.findFiles(boardId, postId);
    }
}
