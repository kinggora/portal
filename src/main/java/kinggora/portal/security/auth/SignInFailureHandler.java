package kinggora.portal.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import kinggora.portal.exception.ErrorCode;
import kinggora.portal.model.error.ErrorResponse;
import kinggora.portal.security.exception.IllegalAuthArgumentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 로그인 인증 실패 핸들러
 * AuthenticationException 예외 응답을 JSON 형태로 반환
 */
@Slf4j
@RequiredArgsConstructor
public class SignInFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    /**
     * 로그인 인증 실패 응답
     * AuthenticationException에 대한 ErrorCode 기반으로 에러 응답 객체 생성
     * ObjectMapper를 통해 에러 응답 객체를 직렬화하여 반환
     * HTTP Status: 401 (Unauthorized)
     *
     * @param request   인증 요청 HTTP Request
     * @param response  Response
     * @param exception 핸들러 호출 원인
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        log.error("SignInFailureHandler.onAuthenticationFailure", exception);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ErrorCode errorCode = getErrorCode(exception);
        try {
            String json = objectMapper.writeValueAsString(ErrorResponse.of(errorCode));
            response.getWriter().write(json);
        } catch (IOException e) {
            log.error("SignInFailureHandler.onAuthenticationFailure", e);
        }
    }

    /**
     * AuthenticationException 서브 클래스에 대해 API Error Code 매핑
     *
     * @param ex 인증 처리 중 발생한 예외
     * @return ex에 매핑된 API Error Code
     */
    private ErrorCode getErrorCode(AuthenticationException ex) {
        if (ex instanceof BadCredentialsException) {
            return ErrorCode.INVALID_CREDENTIAL;
        } else if (ex instanceof AccountExpiredException || ex instanceof CredentialsExpiredException) {
            return ErrorCode.EXPIRED_USER;
        } else if (ex instanceof DisabledException) {
            return ErrorCode.DISABLED_USER;
        } else if (ex instanceof LockedException) {
            return ErrorCode.LOCKED_USER;
        } else if (ex instanceof IllegalAuthArgumentException) {
            return ErrorCode.EMPTY_AUTH_PARAMETER;
        } else {
            return ErrorCode.AUTHENTICATION_ERROR;
        }
    }
}
