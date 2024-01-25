package kinggora.portal.domain;

import kinggora.portal.domain.type.FileType;
import lombok.*;

import java.time.LocalDateTime;

/**
 * file 테이블 Domain Class
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UploadFile {

    private Integer id;
    private Integer postId;
    private String origName;
    private String storeName;
    private String ext;
    private long size;
    private String url;
    private FileType type;
    private boolean deleted;
    private LocalDateTime regDate;
}
