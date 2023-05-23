package kinggora.portal.util;

import kinggora.portal.api.ErrorCode;
import kinggora.portal.exception.BizException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static String getCurrentUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new BizException(ErrorCode.INVALID_AUTH_TOKEN);
        }
        return authentication.getName();
    }
}
