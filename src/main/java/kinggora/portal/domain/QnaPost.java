package kinggora.portal.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QnaPost {

    private Integer id;
    private Member member;
    private Category category;
    private Integer parent;
    private String title;
    private String content;
    private int hit;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
}
