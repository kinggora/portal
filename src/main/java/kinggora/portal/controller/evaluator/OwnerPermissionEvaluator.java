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


    public boolean hasPermissionToPost(Object targetDomainObject) {
        if (!(targetDomainObject instanceof Id)) {
            return false;
        }
        Id postId = (Id) targetDomainObject;
        Post post = boardsService.findPostById(postId.getId());
        if (SecurityUtil.getCurrentUsername().equals(post.getMember().getUsername())) {
            return true;
        }
        return false;
    }

    public boolean hasPermissionToComment(Object targetDomainObject) {
        if (!(targetDomainObject instanceof Id)) {
            return false;
        }
        Id commentId = (Id) targetDomainObject;
        Comment comment = commentService.findCommentById(commentId.getId());
        if (SecurityUtil.getCurrentUsername().equals(comment.getMember().getUsername())) {
            return true;
        }
        return false;
    }

    public boolean hasPermissionToFile(Object targetDomainObject) {
        if (!(targetDomainObject instanceof Id)) {
            return false;
        }
        Id fileId = (Id) targetDomainObject;
        Post post = boardsService.findPostByFileId(fileId.getId());
        if (SecurityUtil.getCurrentUsername().equals(post.getMember().getUsername())) {
            return true;
        }
        return false;
    }

}
