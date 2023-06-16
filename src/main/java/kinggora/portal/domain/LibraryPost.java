package kinggora.portal.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LibraryPost {
    private Integer id;
    private Integer boardId;
    private Member member;
    private String title;
    private String content;
    private int hit;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
}
