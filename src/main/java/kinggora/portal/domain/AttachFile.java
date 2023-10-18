package kinggora.portal.domain;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttachFile {

    private Integer id;
    private Integer postId;
    private String origName;
    private String storeDir;
    private String storeName;
    private String ext;
    private long size;
    private boolean isThumbnail;
}
