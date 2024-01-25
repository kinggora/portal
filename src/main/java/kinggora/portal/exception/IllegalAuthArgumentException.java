package kinggora.portal.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

/**
 * 로그인 인증 과정에서 요청 파라미터가 null이거나 빈 문자열일 경우 임의로 발생시키는 AuthenticationException
 */
@Getter
public class IllegalAuthArgumentException extends AuthenticationException {

    public IllegalAuthArgumentException(String msg) {
        super(msg);
    }
}
