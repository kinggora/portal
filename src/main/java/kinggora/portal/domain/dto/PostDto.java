package kinggora.portal.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDto {
    private Integer id;
    private Integer memberId;
    private Integer categoryId;
    private String title;
    private String content;
    private int hit = 0;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
}
