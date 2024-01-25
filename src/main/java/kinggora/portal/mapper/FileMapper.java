package kinggora.portal.mapper;

import kinggora.portal.domain.UploadFile;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

/**
 * MyBatis Mapper Interface
 * FileMapper.xml에 정의된 SQL과 메서드를 매핑
 */
@Mapper
public interface FileMapper {

    int saveFiles(List<UploadFile> files);

    Optional<UploadFile> findById(int id);

    List<UploadFile> findByPostId(int postId);

    int deleteById(int id);

    int deleteByPostId(int postId);

}
