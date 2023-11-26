package kinggora.portal.exception;

import kinggora.portal.api.ErrorCode;
import kinggora.portal.api.ErrorField;
import kinggora.portal.api.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice(basePackages = "kinggora.portal.controller")
public class PortalExceptionHandler {

    @ExceptionHandler(BizException.class)
    public ResponseEntity<ErrorResponse> bizExceptionHandler(BizException e) {
        log.error("PortalExceptionHandler.BizException", e);
        ErrorCode error = e.getCode();
        String message = e.getMessage();
        if (message == null) {
            // ErrorCode 의 default message로 ErrorResponse의 message 필드 초기화
            return ResponseEntity.status(error.getStatus())
                    .body(ErrorResponse.of(error));
        } else {
            return ResponseEntity.status(error.getStatus())
                    .body(ErrorResponse.of(error, message));
        }
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse badCredentialsExceptionHandler(Exception e) {
        log.error("PortalExceptionHandler.BadCredentialsException", e);
        return ErrorResponse.of(ErrorCode.UNAUTHORIZED_MEMBER);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse usernameNotFoundExceptionHandler(UsernameNotFoundException e) {
        log.error("PortalExceptionHandler.UsernameNotFoundException", e);
        return ErrorResponse.of(ErrorCode.UNAUTHORIZED_MEMBER);
    }

    /**
     * ModelAttribute binding error
     * BindingResult 로부터 binding error 가 발생한 field 정보를 response 에 포함하여 반환
     *
     * @param e BindException
     * @return ErrorResponse
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse bindExceptionHandler(BindException e) {
        log.error("PortalExceptionHandler.BindException", e);
        return ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, ErrorField.of(e.getBindingResult()));
    }

    /**
     * RequestBody, RequestPart binding error
     * BindingResult 로부터 binding error 가 발생한 field 정보를 response 에 포함하여 반환
     *
     * @param e MethodArgumentNotValidException
     * @return ErrorResponse
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error("PortalExceptionHandler.MethodArgumentNotValidException", e);
        return ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, ErrorField.of(e.getBindingResult()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse methodArgumentNotValidExceptionHandler(MethodArgumentTypeMismatchException e) {
        log.error("PortalExceptionHandler.methodArgumentNotValidExceptionHandler", e);
        return ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getMessage());
    }

    /**
     * 상위 handler 들이 다루지 않는 common exception 핸들링
     *
     * @param e Exception
     * @return ErrorResponse
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse commonExceptionHandler(Exception e) {
        log.error("PortalExceptionHandler.Exception", e);
        return ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
