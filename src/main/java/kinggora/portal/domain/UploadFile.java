package kinggora.portal.domain;

import kinggora.portal.domain.type.FileType;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UploadFile {

    private Integer id;
    private Integer postId;
    private String origName;
    private String storeDir;
    private String storeName;
    private String ext;
    private long size;
    private FileType type;
}
