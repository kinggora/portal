package kinggora.portal.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * boards 테이블 Domain Object
 */
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    private Integer id;
    private Integer boardId;
    private Integer memberId;
    private Integer categoryId;
    private String title;
    private String content;
    private int hit;
    private Integer parent;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private Boolean secret;
    private boolean deleted;
}
