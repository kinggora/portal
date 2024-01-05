package kinggora.portal.exception.handler;

import kinggora.portal.api.ErrorCode;
import kinggora.portal.api.ErrorField;
import kinggora.portal.api.ErrorResponse;
import kinggora.portal.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice(basePackages = "kinggora.portal.controller")
public class PortalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * 비지니스 로직 수행 도중 발생한 예외 처리
     *
     * @param e BizException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(BizException.class)
    public ResponseEntity<ErrorResponse> handleBizException(BizException e) {
        log.error("PortalExceptionHandler.handleBizException", e);
        ErrorCode error = e.getCode();
        String message = e.getMessage();
        if (message == null) {
            return ResponseEntity.status(error.getStatus())
                    .body(ErrorResponse.of(error));
        } else {
            return ResponseEntity.status(error.getStatus())
                    .body(ErrorResponse.of(error, message));
        }
    }

    /**
     * 로그인 중 발생하는 예외 처리 (인증)
     * Http Status: 401 (Unauthorized)
     *
     * @param ex AuthenticationException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        log.error("PortalExceptionHandler.handleAuthenticationException", ex);
        ErrorCode errorCode;
        if (ex instanceof BadCredentialsException || ex instanceof UsernameNotFoundException) {
            errorCode = ErrorCode.INVALID_CREDENTIAL;
        } else if (ex instanceof AccountExpiredException || ex instanceof CredentialsExpiredException) {
            errorCode = ErrorCode.EXPIRED_USER;
        } else if (ex instanceof DisabledException) {
            errorCode = ErrorCode.DISABLED_USER;
        } else if (ex instanceof LockedException) {
            errorCode = ErrorCode.LOCKED_USER;
        } else {
            errorCode = ErrorCode.AUTHENTICATION_ERROR;
        }
        return ResponseEntity
                .status(errorCode.getStatus().value())
                .body(ErrorResponse.of(errorCode));
    }

    /**
     * 요청한 리소스에 대한 접근 권한이 없을 때 발생한 예외 처리 (인가)
     * BoardsController 에서 @PreAuthorize 에 의해 AccessDeniedException 발생 시 처리한다.
     * Http Status: 403 (Forbidden)
     *
     * @param e AccessDeniedException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        log.error("PortalExceptionHandler.handleAccessDeniedException", e);
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED_ACCESS;
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.of(errorCode));
    }

    /**
     * ModelAttribute binding error
     * BindingResult 로부터 binding error 가 발생한 field 정보를 response 에 포함하여 반환
     *
     * @param ex BindException
     * @return ResponseEntity<ErrorResponse>
     */
    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("PortalExceptionHandler.handleBindException", ex);
        ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;
        return ResponseEntity
                .status(errorCode.getStatus().value())
                .body(ErrorResponse.of(errorCode, ErrorField.of(ex.getBindingResult())));
    }

    /**
     * RequestBody, RequestPart binding error
     * BindingResult 로부터 binding error 가 발생한 field 정보를 response 에 포함하여 반환
     *
     * @param ex MethodArgumentNotValidException
     * @return ResponseEntity<ErrorResponse>
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("PortalExceptionHandler.handleMethodArgumentNotValid", ex);
        ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;
        return ResponseEntity
                .status(errorCode.getStatus().value())
                .body(ErrorResponse.of(errorCode, ErrorField.of(ex.getBindingResult())));
    }

    /**
     * @param ex TypeMismatchException
     * @return ResponseEntity<ErrorResponse>
     */
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("PortalExceptionHandler.handleTypeMismatch", ex);
        ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;
        return ResponseEntity
                .status(errorCode.getStatus().value())
                .body(ErrorResponse.of(errorCode, "입력 값의 타입이 올바르지 않습니다."));
    }

    /**
     * common exception handler
     * HTTP Status: 500 (Internal Server Error)
     *
     * @param ex Exception
     * @return ResponseEntity<ErrorResponse>
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity
                .status(errorCode.getStatus().value())
                .body(ErrorResponse.of(errorCode));
    }
}
