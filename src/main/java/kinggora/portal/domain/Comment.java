package kinggora.portal.domain;

import lombok.*;

import java.time.LocalDateTime;

/**
 * comment 테이블 Domain Class
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    private Integer id;
    private Integer postId;
    private Integer memberId;
    private String content;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private Integer ref;
    private Integer refOrder;
    private Integer parent;
    private Integer depth;
    private boolean deleted;

}
