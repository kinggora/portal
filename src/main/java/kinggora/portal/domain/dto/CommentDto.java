package kinggora.portal.domain.dto;

import kinggora.portal.domain.Comment;
import kinggora.portal.domain.Member;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private Integer id;
    private Integer postId;
    private Integer memberId;
    private String content;
    private Integer parent;

    public Comment toComment() {
        return Comment.builder()
                .id(id)
                .postId(postId)
                .member(new Member(memberId))
                .content(content)
                .parent(parent)
                .build();
    }

    public Comment toRootComment(int ref) {
        return Comment.builder()
                .parent(null)
                .member(new Member(memberId))
                .content(content)
                .postId(postId)
                .regDate(LocalDateTime.now())
                .depth(0)
                .ref(ref)
                .refOrder(0)
                .delFlag(false)
                .build();
    }

    public Comment toChildComment(int depth, int ref, int refOrder) {
        return Comment.builder()
                .parent(parent)
                .member(new Member(memberId))
                .content(content)
                .postId(postId)
                .regDate(LocalDateTime.now())
                .depth(depth)
                .ref(ref)
                .refOrder(refOrder)
                .delFlag(false)
                .build();
    }

}