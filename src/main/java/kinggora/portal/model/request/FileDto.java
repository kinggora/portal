package kinggora.portal.model.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * 파일 DTO
 * - CREATE 요청에 사용
 * - 첨부 파일, 본문 이미지 파일 폼 데이터 정의
 */
@Data
public class FileDto {

    private List<MultipartFile> attachment = new ArrayList<>();
    private List<MultipartFile> content = new ArrayList<>();

}
