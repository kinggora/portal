package kinggora.portal.model.data.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 댓글 Response Object
 * 게시글 상세에서 나타낼 정보 포함
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PostComment {

    private Integer id;
    private Integer postId;
    private MemberResponse member;
    private String content;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private Integer depth;
    private boolean deleted;
}
