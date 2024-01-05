package kinggora.portal.domain.dto.request;

import kinggora.portal.domain.Comment;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CommentDto {

    @NotBlank
    @Size(min = 1, max = 1000)
    private String content;
    private int postId;
    private int memberId;

    public void injectIds(int postId, int memberId) {
        this.postId = postId;
        this.memberId = memberId;
    }

    public Comment toUpdateComment(int id) {
        return Comment.builder()
                .id(id)
                .content(content)
                .build();
    }


    public Comment toRootComment(int ref) {
        return Comment.builder()
                .parent(null)
                .memberId(memberId)
                .content(content)
                .postId(postId)
                .depth(0)
                .ref(ref)
                .refOrder(0)
                .deleted(false)
                .build();
    }

    public Comment toChildComment(int refOrder, Comment parent) {
        int depth = parent.getDepth() + 1;
        int ref = parent.getRef();
        return Comment.builder()
                .parent(parent.getId())
                .memberId(memberId)
                .content(content)
                .postId(postId)
                .depth(depth)
                .ref(ref)
                .refOrder(refOrder)
                .deleted(false)
                .build();
    }

}
