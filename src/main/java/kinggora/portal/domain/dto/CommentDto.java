package kinggora.portal.domain.dto;

import kinggora.portal.domain.Comment;
import kinggora.portal.domain.Member;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private Integer id;
    @NotNull
    private Integer postId;
    private Integer memberId;
    @NotBlank
    @Size(min = 1, max = 1000)
    private String content;

    public Comment toUpdateComment() {
        return Comment.builder()
                .id(id)
                .postId(postId)
                .content(content)
                .build();
    }

    public Comment toRootComment(int ref) {
        return Comment.builder()
                .parent(null)
                .member(new Member(memberId))
                .content(content)
                .postId(postId)
                .depth(0)
                .ref(ref)
                .refOrder(0)
                .deleted(false)
                .build();
    }

    public Comment toChildComment(Comment parent, int refOrder) {
        int depth = parent.getDepth() + 1;
        int ref = parent.getRef();
        return Comment.builder()
                .parent(parent.getId())
                .member(new Member(memberId))
                .content(content)
                .postId(postId)
                .depth(depth)
                .ref(ref)
                .refOrder(refOrder)
                .deleted(false)
                .build();
    }

}
