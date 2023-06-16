package kinggora.portal.domain.dto;

import kinggora.portal.domain.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDto {
    private Integer id;
    private Integer memberId;
    private Integer categoryId;
    private Integer boardId;
    private Integer parent;
    private String title;
    private String content;
    private int hit = 0;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private List<MultipartFile> newFiles = new ArrayList<>();

    public FreePost toFreePost() {
        return FreePost.builder()
                .id(id)
                .member(new Member(memberId))
                .category(new Category(categoryId))
                .title(title)
                .content(content)
                .hit(hit)
                .regDate(regDate)
                .modDate(modDate)
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
                .hit(hit)
                .regDate(regDate)
                .modDate(modDate)
                .build();
    }

    public GalleryPost toGalleryPost() {
        return GalleryPost.builder()
                .id(id)
                .member(new Member(memberId))
                .title(title)
                .hit(hit)
                .regDate(regDate)
                .modDate(modDate)
                .build();
    }

    public LibraryPost toLibraryPost() {
        return LibraryPost.builder()
                .id(id)
                .boardId(boardId)
                .member(new Member(memberId))
                .title(title)
                .content(content)
                .hit(hit)
                .regDate(regDate)
                .modDate(modDate)
                .build();
    }
}
