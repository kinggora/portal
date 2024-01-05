package kinggora.portal.domain.dto.request;

import kinggora.portal.annotation.ByteSize;
import kinggora.portal.domain.Post;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class PostDto {

    @NotNull
    private Integer categoryId;
    @NotNull
    @ByteSize(min = 1, max = 300, message = "제목을 300byte(한글 100자) 이내로 입력하세요.")
    private String title;
    @ByteSize(max = 6000, message = "내용을 6000byte(한글 2000자) 이내로 입력하세요.")
    private String content;
    @NotNull
    private Boolean secret;

    public Post toRootPost(int boardId, int memberId) {
        return Post.builder()
                .memberId(memberId)
                .boardId(boardId)
                .categoryId(categoryId)
                .title(title)
                .content(content)
                .secret(secret)
                .regDate(LocalDateTime.now())
                .build();
    }

    public Post toChildPost(Post parent, int memberId) {
        return Post.builder()
                .memberId(memberId)
                .boardId(parent.getBoardId())
                .categoryId(parent.getCategoryId())
                .parent(parent.getId())
                .title(title)
                .content(content)
                .secret(parent.getSecret())
                .regDate(LocalDateTime.now())
                .build();
    }

    public Post toUpdatePost(int id) {
        return Post.builder()
                .id(id)
                .categoryId(categoryId)
                .title(title)
                .content(content)
                .secret(secret)
                .modDate(LocalDateTime.now())
                .build();
    }
}
