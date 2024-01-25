package kinggora.portal.util;

import kinggora.portal.domain.Member;
import kinggora.portal.exception.BizException;
import kinggora.portal.exception.ErrorCode;
import kinggora.portal.security.user.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Spring Security 유틸리티
 * Authentication 관련 get 메서드 정의
 */
@Component
public class SecurityUtil {

    /**
     * private 생성자
     */
    private SecurityUtil() {
    }

    /**
     * Spring Security 필터 단에서 SecurityContext에 저장된 Authentication 객체 반환
     *
     * @return
     */
    private Authentication getAuthentication() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new BizException(ErrorCode.INVALID_AUTH_TOKEN);
        }
        return authentication;
    }

    public Member getCurrentMember() {
        return ((CustomUserDetails) getAuthentication().getPrincipal()).getMember();
    }

    public Integer getCurrentMemberId() {
        return getCurrentMember().getId();
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getAuthentication().getAuthorities();
    }

}
