package kinggora.portal.security.exception;

import kinggora.portal.exception.ErrorCode;
import lombok.Getter;

/**
 * JWT 인증 과정에서 발생하는 예외
 * API 응답을 위해 ErrorCode 정의
 */
@Getter
public class JwtAuthException extends RuntimeException {
    private final ErrorCode code;

    public JwtAuthException(ErrorCode code, Throwable throwable) {
        super(code.getDefaultMessage(), throwable);
        this.code = code;
    }

}
