package kinggora.portal.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
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
