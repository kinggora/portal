package kinggora.portal.mapper;

import kinggora.portal.domain.AttachFile;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface FileMapper {

    /**
     * DB에 첨부 파일 정보 저장
     *
     * @param files 파일 정보 리스트
     */
    int saveFile(List<AttachFile> files);

    /**
     * 파일 정보 단건 조회
     *
     * @param id 파일 id
     * @return 파일 정보
     */
    Optional<AttachFile> findFileById(Integer id);

    /**
     * 해당 게시글에 첨부된 파일 정보 조회
     *
     * @param postId 게시글 id
     * @return 파일 정보 리스트
     */
    List<AttachFile> findFiles(Integer postId);

    int deleteFiles(Integer boardId, Integer postId);
}
