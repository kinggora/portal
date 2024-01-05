package kinggora.portal.domain.dto.response;

import kinggora.portal.domain.BoardInfo;
import kinggora.portal.domain.Category;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BoardDetail {
    private Integer id;
    private BoardInfo boardInfo;
    private MemberResponse member;
    private Category category;
    private String title;
    private String content;
    private int hit;
    private Integer parent;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private boolean secret;
    private boolean commentExists;
    private boolean fileExists;
    private boolean childExists;
    private boolean deleted;
}
