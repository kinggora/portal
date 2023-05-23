package kinggora.portal.exception;

import kinggora.portal.api.ErrorCode;
import kinggora.portal.api.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "kinggora.portal.controller")
public class PortalExceptionHandler {

    @ExceptionHandler(BizException.class)
    public ResponseEntity<ErrorResponse> bizExceptionHandler(BizException e) {
        log.error("BizException", e);
        ErrorCode error = e.getCode();
        String message = e.getMessage();
        if(message == null) {
            return ResponseEntity.status(error.getStatus())
                    .body(ErrorResponse.of(error));
        } else {
            return ResponseEntity.status(error.getStatus())
                    .body(ErrorResponse.of(error, message));
        }
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse usernameNotFoundExceptionHandler(UsernameNotFoundException e) {
        log.error("UsernameNotFoundException", e);
        return ErrorResponse.of(ErrorCode.UNAUTHORIZED_MEMBER);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse commonExceptionHandler(Exception e) {
        log.error("Exception", e);
        return ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
