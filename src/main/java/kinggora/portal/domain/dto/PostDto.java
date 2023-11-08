package kinggora.portal.domain.dto;

import kinggora.portal.domain.*;
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


    public CommonPost toCommonPost() {
        return CommonPost.builder()
                .id(id)
                .boardInfo(new BoardInfo(boardId))
                .member(new Member(memberId))
                .category(new Category(categoryId))
                .title(title)
                .content(content)
                .build();
    }

    public QnaPost toQnaPost() {
        return QnaPost.builder()
                .id(id)
                .member(new Member(memberId))
                .category(new Category(categoryId))
                .title(title)
                .content(content)
                .parent(parent)
                .build();
    }
}
