package kinggora.portal.controller.api.error;

import kinggora.portal.controller.api.ApiResponse;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 요청 처리 중 예외 발생 시 반환하는 API Response Object
 */
@Getter
public class ErrorResponse extends ApiResponse {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private List<ErrorField> errors;

    private ErrorResponse(ErrorCode code) {
        super(code.getCode(), code.getDefaultMessage());
    }

    private ErrorResponse(ErrorCode code, String message) {
        super(code.getCode(), message);
    }

    private ErrorResponse(ErrorCode code, List<ErrorField> errors) {
        super(code.getCode(), code.getDefaultMessage());
        this.errors = errors;
    }

    /**
     * ErrorCode 로 ErrorResponse 생성
     * ErrorCode 의 DefaultMessage 로 message 를 초기화
     *
     * @param code 에러 코드
     * @return ErrorResponse
     */
    public static ErrorResponse of(ErrorCode code) {
        return new ErrorResponse(code);
    }

    /**
     * ErrorCode와 message로 ErrorResponse 생성
     *
     * @param code    에러 코드
     * @param message 에러 메시지
     * @return ErrorResponse
     */
    public static ErrorResponse of(ErrorCode code, String message) {
        return new ErrorResponse(code, message);
    }

    /**
     * ErrorCode와 ErrorField 리스트로 ErrorResponse 생성
     *
     * @param code   에러 코드
     * @param errors 에러 필드
     * @return ErrorResponse
     */

    public static ErrorResponse of(ErrorCode code, List<ErrorField> errors) {
        return new ErrorResponse(code, errors);
    }

}
