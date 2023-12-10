package kinggora.portal.controller.evaluator;

import kinggora.portal.domain.BoardInfo;
import kinggora.portal.domain.dto.request.Id;
import kinggora.portal.domain.type.AccessLevel;
import kinggora.portal.domain.type.MemberRole;
import kinggora.portal.service.BoardInfoService;
import kinggora.portal.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@Component("accessPermissionEvaluator")
public class AccessPermissionEvaluator {

    private final BoardInfoService boardInfoService;

    public boolean hasPermissionToPost(Object targetDomainObject, Object permission) {
        if (!(targetDomainObject instanceof Id) || !(permission instanceof String)) {
            return false;
        }
        Id postId = (Id) targetDomainObject;
        BoardInfo boardInfo = boardInfoService.findBoardInfoByPostId(postId.getId());
        return checkPermission(boardInfo, (String) permission);
    }

    public boolean hasPermissionToBoard(Object targetDomainObject, Object permission) {
        if (!(targetDomainObject instanceof Id) || !(permission instanceof String)) {
            return false;
        }
        Id boardId = (Id) targetDomainObject;
        BoardInfo boardInfo = boardInfoService.findBoardInfoById(boardId.getId());
        return checkPermission(boardInfo, (String) permission);
    }

    private boolean checkPermission(BoardInfo boardInfo, String permission) {
        switch ((permission).toUpperCase()) {
            case "LIST":
                return authorization(boardInfo.getAccessList());
            case "READ":
                return authorization(boardInfo.getAccessRead());
            case "WRITE":
                return authorization(boardInfo.getAccessWrite());
            case "REPLY-READ":
                return authorization(boardInfo.getAccessReplyRead());
            case "REPLY-WRITE":
                return authorization(boardInfo.getAccessReplyWrite());
            case "COMMENT":
                return authorization(boardInfo.getAccessComment());
            case "FILE":
                return authorization(boardInfo.getAccessFile());
        }
        return false;
    }

    private boolean authorization(AccessLevel accessLevel) {
        if (AccessLevel.ALL.equals(accessLevel)) {
            return true;
        }
        if (AccessLevel.NONE.equals(accessLevel)) {
            return false;
        }
        if (AccessLevel.USER.equals(accessLevel)) {
            return findAuthority(SecurityUtil.getAuthorities(), MemberRole.USER.getCode());
        }
        if (AccessLevel.ADMIN.equals(accessLevel)) {
            return findAuthority(SecurityUtil.getAuthorities(), MemberRole.ADMIN.getCode());
        }
        return false;
    }

    private boolean findAuthority(Collection<? extends GrantedAuthority> authorities, String authority) {
        return authorities.stream().anyMatch(
                auth -> auth.getAuthority().equals(authority)
        );
    }
}
