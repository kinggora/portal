package kinggora.portal.domain.dto.response;

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
public class BoardItem {
    private Integer boardId;
    private Integer postId;
    private String memberName;
    private String categoryName;
    private String title;
    private int hit;
    private LocalDateTime regDate;
}
