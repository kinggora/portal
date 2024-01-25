package kinggora.portal.model.data.response;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 댓글 Response Object
 */
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CommentResponse {

    private Integer id;
    private Integer postId;
    private MemberResponse member;
    private String content;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private Integer depth;
    private boolean deleted;
}
