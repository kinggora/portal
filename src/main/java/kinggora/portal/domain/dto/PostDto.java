package kinggora.portal.domain.dto;

import kinggora.portal.domain.BoardInfo;
import kinggora.portal.domain.Category;
import kinggora.portal.domain.Member;
import kinggora.portal.domain.Post;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private Integer id;
    private Integer memberId;
    private Integer categoryId;
    private Integer boardId;
    private Integer parent;
    private String title;
    private String content;


    public Post toPost() {
        return Post.builder()
                .id(id)
                .boardInfo(new BoardInfo(boardId))
                .member(new Member(memberId))
                .category(new Category(categoryId))
                .title(title)
                .content(content)
                .parent(parent)
                .build();
    }
}
