package kinggora.portal.web.evaluator;

import kinggora.portal.domain.Comment;
import kinggora.portal.domain.Post;
import kinggora.portal.model.data.request.Id;
import kinggora.portal.service.BoardService;
import kinggora.portal.service.CommentService;
import kinggora.portal.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 게시판 Owner Permission Evaluator 클래스
 * - 컨트롤러 단에서 @PreAuthorize 에 의해 호출되며, API 요청에 대해 메서드 레벨로 권한 체크
 * - Writer만 접근 가능한 리소스에 대해 요청한 사용자가 소유권이 있는지 확인
 */
@RequiredArgsConstructor
@Component("ownerPermissionEvaluator")
public class OwnerPermissionEvaluator {
    private final BoardService boardService;
    private final CommentService commentService;
    private final SecurityUtil securityUtil;

    /**
     * 게시글에 대한 소유권 확인
     * 게시글의 작성자와 인증 객체의 id 값을 비교하여 동일하다면 true 반환
     *
     * @param targetDomainObject 권한을 확인할 도메인 객체
     * @return 인가 결과
     */
    public boolean hasPermissionToPost(Object targetDomainObject) {
        if (!(targetDomainObject instanceof Id)) {
            return false;
        }
        Id postId = (Id) targetDomainObject;
        Post post = boardService.findPostById(postId.getId());
        return securityUtil.getCurrentMemberId().equals(post.getMemberId());
    }

    /**
     * 댓글에 대한 소유권 확인
     * 댓글의 작성자와 인증 객체의 id 값을 비교하여 동일하다면 true 반환
     *
     * @param targetDomainObject 권한을 확인할 도메인 객체
     * @return 인가 결과
     */
    public boolean hasPermissionToComment(Object targetDomainObject) {
        if (!(targetDomainObject instanceof Id)) {
            return false;
        }
        Id commentId = (Id) targetDomainObject;
        Comment comment = commentService.findCommentById(commentId.getId());
        return securityUtil.getCurrentMemberId().equals(comment.getMemberId());
    }

    /**
     * 파일에 대한 소유권 확인
     * 첨부 파일은 게시글에 종속되기 때문에 게시글 작성자에게 파일의 소유권이 있다.
     * 게시글 작성자와 인증 객체의 id 값을 비교하여 동일하다면 true 반환
     *
     * @param targetDomainObject 권한을 확인할 도메인 객체
     * @return 인가 결과
     */
    public boolean hasPermissionToFile(Object targetDomainObject) {
        if (!(targetDomainObject instanceof Id)) {
            return false;
        }
        Id fileId = (Id) targetDomainObject;
        Post post = boardService.findPostByFileId(fileId.getId());
        return securityUtil.getCurrentMemberId().equals(post.getMemberId());
    }

}
