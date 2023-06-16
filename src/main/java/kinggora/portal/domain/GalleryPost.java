package kinggora.portal.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class GalleryPost {
    private Integer id;
    private Member member;
    private String title;
    private int hit;
    private LocalDateTime regDate;
    private LocalDateTime modDate;

    public GalleryPost(Integer id) {
        this.id = id;
    }
}
