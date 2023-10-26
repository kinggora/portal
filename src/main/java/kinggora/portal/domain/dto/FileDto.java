package kinggora.portal.domain.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileDto {
    private Integer postId;
    private List<MultipartFile> newFiles = new ArrayList<>();
}