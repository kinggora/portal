package kinggora.portal.model.request;

import kinggora.portal.annotation.ByteSize;
import kinggora.portal.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

/**
 * 게시글 DTO
 * - 컨트롤러 단에서 @ModelAttribute 로 바인딩되어 Bean Validation 수행
 * - 게시글 도메인 객체(Post)로 변환
 * - CREATE, UPDATE 요청에 사용
 */
@Getter
@Builder
@AllArgsConstructor
public class PostDto {

    @NotNull(message = "{require.post.categoryId}")
    @Positive(message = "{positive.post.categoryId}")
    private int categoryId;
    @NotNull(message = "{require.post.title}")
    @ByteSize(min = 1, max = 300, message = "{length.post.title}")
    private String title;
    @NotNull(message = "{require.post.content}")
    @ByteSize(max = 6000, message = "{length.post.content}")
    private String content;
    @NotNull(message = "{require.post.secret}")
    private Boolean secret;

    /**
     * 루트 게시글 등록시 DTO -> 도메인 변환
     * parent = null
     *
     * @param boardId  등록할 게시판 id
     * @param memberId 작성자 id
     * @return 등록할 도메인 객체
     */
    public Post toRootPost(int boardId, int memberId) {
        return Post.builder()
                .memberId(memberId)
                .boardId(boardId)
                .categoryId(categoryId)
                .parent(null)
                .title(title)
                .content(content)
                .regDate(LocalDateTime.now())
                .modDate(null)
                .secret(secret)
                .build();
    }

    /**
     * 자식 게시글 등록시 DTO -> 도메인 변환
     * 다음 필드는 부모의 데이터 기반으로 초기화
     * parent = parent.id
     * boardId = parent.boardId
     * categoryId = parent.categoryId
     * secret = parent.secret
     *
     * @param parent   부모 게시글
     * @param memberId 작성자 id
     * @return 등록할 도메인 객체
     */
    public Post toChildPost(Post parent, int memberId) {
        return Post.builder()
                .memberId(memberId)
                .boardId(parent.getBoardId())
                .categoryId(parent.getCategoryId())
                .parent(parent.getId())
                .title(title)
                .content(content)
                .regDate(LocalDateTime.now())
                .modDate(null)
                .secret(parent.getSecret())
                .build();
    }

    /**
     * 게시글 수정시 DTO -> 도메인 변환
     * 수정 날짜(modDate) 초기화
     *
     * @param id 수정할 게시글 id
     * @return 수정할 도메인 객체
     */
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
