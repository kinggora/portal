package kinggora.portal.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

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
}
