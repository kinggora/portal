package kinggora.portal.model.data.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 댓글 Response Object
 * 회원 페이지에서 나타낼 댓글 정보 포함
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyComment {

    private int id;
    private int postId;
    private String postTitle;
    private String content;
    private LocalDateTime regDate;

}
