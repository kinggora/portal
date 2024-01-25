package kinggora.portal.exception;

import kinggora.portal.controller.api.error.ErrorCode;
import lombok.Getter;

/**
 * 비지니스 로직 실행 중 임의로 발생시킨 예외
 * ErrorCode로 예외 정의
 */
@Getter
public class BizException extends RuntimeException {

    private final ErrorCode code;

    public BizException(ErrorCode code) {
        super(code.getDefaultMessage());
        this.code = code;
    }

    public BizException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }
}
