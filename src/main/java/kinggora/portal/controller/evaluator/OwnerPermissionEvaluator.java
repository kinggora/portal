package kinggora.portal.controller.evaluator;

import kinggora.portal.domain.Comment;
import kinggora.portal.domain.Post;
import kinggora.portal.domain.dto.request.Id;
import kinggora.portal.service.BoardsService;
import kinggora.portal.service.CommentService;
import kinggora.portal.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("ownerPermissionEvaluator")
public class OwnerPermissionEvaluator {
    private final BoardsService boardsService;
    private final CommentService commentService;
    private final SecurityUtil securityUtil;


    public boolean hasPermissionToPost(Object targetDomainObject) {
        if (!(targetDomainObject instanceof Id)) {
            return false;
        }
        Id postId = (Id) targetDomainObject;
        Post post = boardsService.findPostById(postId.getId());
        return securityUtil.getCurrentMemberId().equals(post.getMemberId());
    }

    public boolean hasPermissionToComment(Object targetDomainObject) {
        if (!(targetDomainObject instanceof Id)) {
            return false;
        }
        Id commentId = (Id) targetDomainObject;
        Comment comment = commentService.findCommentById(commentId.getId());
        return securityUtil.getCurrentMemberId().equals(comment.getMemberId());
    }

    public boolean hasPermissionToFile(Object targetDomainObject) {
        if (!(targetDomainObject instanceof Id)) {
            return false;
        }
        Id fileId = (Id) targetDomainObject;
        Post post = boardsService.findPostByFileId(fileId.getId());
        return securityUtil.getCurrentMemberId().equals(post.getMemberId());
    }

}
