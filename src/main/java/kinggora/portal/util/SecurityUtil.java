package kinggora.portal.util;

import kinggora.portal.api.ErrorCode;
import kinggora.portal.domain.Member;
import kinggora.portal.exception.BizException;
import kinggora.portal.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class SecurityUtil {

    public Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new BizException(ErrorCode.INVALID_AUTH_TOKEN);
        }
        return authentication;
    }

    public Member getCurrentMember() {
        return ((CustomUserDetails) getAuthentication().getPrincipal()).getMember();
    }

    public Integer getCurrentMemberId() {
        return ((CustomUserDetails) getAuthentication().getPrincipal()).getId();
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getAuthentication().getAuthorities();
    }

}
