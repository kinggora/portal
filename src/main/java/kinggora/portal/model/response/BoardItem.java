package kinggora.portal.model.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 게시글 목록 Response Object
 * 게시글 목록 요청에서 공통적으로 조회하는 필드를 정의
 */
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BoardItem {
    private Integer boardId;
    private Integer postId;
    private String memberName;
    private String categoryName;
    private String title;
    private int hit;
    private LocalDateTime regDate;
}
