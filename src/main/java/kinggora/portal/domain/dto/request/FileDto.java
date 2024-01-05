package kinggora.portal.domain.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class FileDto {

    @NotNull
    private List<MultipartFile> attachment = new ArrayList<>();
    @NotNull
    private List<MultipartFile> content = new ArrayList<>();

}
