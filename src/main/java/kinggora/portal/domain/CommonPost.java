package kinggora.portal.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@ToString
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommonPost {

    private Integer id;
    private BoardInfo boardInfo;
    private Member member;
    private Category category;
    private String title;
    private String content;
    private int hit;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private boolean attached; // 첨부 파일 유무
    private boolean imaged; // 컨텐츠 이미지 파일 유무
}
