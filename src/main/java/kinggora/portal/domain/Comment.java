package kinggora.portal.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    private Integer id;
    private Integer postId;
    private Member member;
    private String content;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private Integer ref;
    private Integer refOrder;
    private Integer parent;
    private Integer depth;
    private boolean deleted;
}
