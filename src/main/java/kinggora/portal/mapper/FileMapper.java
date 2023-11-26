package kinggora.portal.mapper;

import kinggora.portal.domain.UploadFile;
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
    int saveFiles(List<UploadFile> files);

    /**
     * 파일 정보 단건 조회
     *
     * @param id 파일 id
     * @return 파일 정보
     */
    Optional<UploadFile> findFileById(int id);

    /**
     * 해당 게시글에 첨부된 파일 정보 조회
     *
     * @param postId 게시글 id
     * @return 파일 정보 리스트
     */
    List<UploadFile> findFiles(int postId);

    //List<UploadFile> findFilesOfType(@Param("postIds") List<Integer> postIds, @Param("type") FileType type);

    int deleteFile(int id);

    int deleteFiles(int postId);
}
