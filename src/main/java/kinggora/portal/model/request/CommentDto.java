package kinggora.portal.model.request;

import kinggora.portal.domain.Comment;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 댓글 DTO
 * - 컨트롤러 단에서 @ModelAttribute 로 바인딩되어 Bean Validation 수행
 * - 댓글 도메인 객체(Comment)로 변환
 * - CREATE, UPDATE, DELETE 요청에 사용
 */
@Getter
public class CommentDto {

    @NotBlank(message = "{require.comment.content}")
    @Size(min = 1, max = 1000, message = "{size.comment.content}")
    private final String content;
    private int postId;
    private int memberId;

    public CommentDto(String content) {
        this.content = content;
    }

    /**
     * 컨트롤러 단에서 Path Variable, 인증 객체로부터 postId, memberId 초기화
     *
     * @param postId   게시글 id
     * @param memberId 작성자 id
     */
    public void injectIds(int postId, int memberId) {
        this.postId = postId;
        this.memberId = memberId;
    }

    /**
     * 루트 댓글 등록시 DTO -> 도메인 변환
     * parent = null
     * depth = 0
     * ref = MAX(ref) + 1
     * refOrder = 0
     *
     * @param ref 댓글 그룹
     * @return 등록할 도메인 객체
     */
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

    /**
     * 자식 댓글 등록시 DTO -> 도메인 변환
     * 다음 필드는 부모의 데이터 기반으로 초기화
     * depth = parent.depth + 1
     * ref = parent.ref
     *
     * @param refOrder 댓글 그룹 내 순서
     * @param parent   부모 댓글
     * @return 등록할 댓글 도메인
     */
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

    /**
     * 댓글 수정시 DTO -> 도메인 변환
     *
     * @param id 수정할 댓글 id
     * @return 수정할 댓글 도메인
     */
    public Comment toUpdateComment(int id) {
        return Comment.builder()
                .id(id)
                .content(content)
                .build();
    }

}