package kinggora.portal.domain.dto;

import kinggora.portal.annotation.ByteSize;
import kinggora.portal.domain.BoardInfo;
import kinggora.portal.domain.Category;
import kinggora.portal.domain.Member;
import kinggora.portal.domain.Post;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private Integer memberId;
    private Integer categoryId;
    private Integer boardId;
    private Integer parent;
    @NotBlank
    @ByteSize(max = 200, message = "제목을 200byte(한글 100자) 이내로 입력하세요.")
    private String title;
    private String content;


    public Post toPost() {
        return Post.builder()
                .boardInfo(new BoardInfo(boardId))
                .member(new Member(memberId))
                .category(new Category(categoryId))
                .title(title)
                .content(content)
                .parent(parent)
                .build();
    }

    public Post toUpdatePost(int id) {
        return Post.builder()
                .id(id)
                .category(new Category(categoryId))
                .title(title)
                .content(content)
                .build();
    }
}
