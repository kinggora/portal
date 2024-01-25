package kinggora.portal.web.evaluator;

import kinggora.portal.domain.BoardInfo;
import kinggora.portal.domain.Post;
import kinggora.portal.domain.type.AccessLevel;
import kinggora.portal.domain.type.MemberRole;
import kinggora.portal.model.data.request.Id;
import kinggora.portal.service.BoardInfoService;
import kinggora.portal.service.BoardService;
import kinggora.portal.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 게시판 Access Permission Evaluator 클래스
 * - 컨트롤러 단에서 @PreAuthorize 에 의해 호출되며, API 요청에 대해 메서드 레벨로 권한 체크
 * - 게시판 정보로부터 요청 리소스에 대한 접근 레벨을 조회하여 사용자에게 권한이 있는지 확인
 */
@Slf4j
@RequiredArgsConstructor
@Component("accessPermissionEvaluator")
public class AccessPermissionEvaluator {

    private final BoardInfoService boardInfoService;
    private final BoardService boardService;
    private final SecurityUtil securityUtil;

    /**
     * 게시판의 정보를 조회하여 Permission에 대한 권한 확인을 위임
     *
     * @param targetDomainObject 권한을 확인할 도메인 객체
     * @param permission         권한을 확인할 내용(표현)
     * @return 인가 결과
     */
    public boolean hasPermissionToBoard(Object targetDomainObject, Object permission) {
        if (!(targetDomainObject instanceof Id) || !(permission instanceof String)) {
            return false;
        }
        Id boardId = (Id) targetDomainObject;
        BoardInfo boardInfo = boardInfoService.findBoardInfoById(boardId.getId());
        return checkPermission(boardInfo, (String) permission);
    }

    /**
     * 게시글로부터 게시판의 정보를 조회하여 Permission에 대한 권한 확인을 위임
     *
     * @param targetDomainObject 권한을 확인할 도메인 객체
     * @param permission         권한을 확인할 내용(표현)
     * @return 인가 결과
     */
    public boolean hasPermissionToPost(Object targetDomainObject, Object permission) {
        if (!(targetDomainObject instanceof Id) || !(permission instanceof String)) {
            return false;
        }
        Id postId = (Id) targetDomainObject;
        Post post = boardService.findPostById(postId.getId());
        BoardInfo boardInfo = boardInfoService.findBoardInfoById(post.getBoardId());
        return checkPermission(boardInfo, (String) permission);
    }

    /**
     * 정의된 Permission 종류에 따라 게시판의 AccessLevel을 조회하여 인가를 위임
     * 정의되지 않은 Permission은 false를 반환
     *
     * @param boardInfo  게시판 정보
     * @param permission 권한을 확인할 내용(표현)
     * @return 인가 결과
     */
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

    /**
     * AccessLevel에 따라 실제 사용자 인가 수행
     *
     * @param accessLevel 접근 레벨
     * @return 인가 결과
     */
    private boolean authorization(AccessLevel accessLevel) {
        if (AccessLevel.ALL.equals(accessLevel)) {
            return true;
        }
        if (AccessLevel.NONE.equals(accessLevel)) {
            return false;
        }
        if (AccessLevel.USER.equals(accessLevel)) {
            return findAuthority(securityUtil.getAuthorities(), MemberRole.USER.getCode());
        }
        if (AccessLevel.ADMIN.equals(accessLevel)) {
            return findAuthority(securityUtil.getAuthorities(), MemberRole.ADMIN.getCode());
        }
        return false;
    }

    /**
     * 권한 컬렉션에서 특정 권한 찾기
     *
     * @param authorities 권한 컬렉션
     * @param authority   탐색 권한
     * @return 존재 여부
     */
    private boolean findAuthority(Collection<? extends GrantedAuthority> authorities, String authority) {
        return authorities.stream().anyMatch(
                auth -> auth.getAuthority().equals(authority)
        );
    }
}
