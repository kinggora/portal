package kinggora.portal.model.data.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 댓글 필터링 조건 정의
 */
@Getter
@Builder
@AllArgsConstructor
public class CommentCriteria {
    private Integer postId;
    private Integer memberId;
    private Boolean deleted;
}
