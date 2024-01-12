package kinggora.portal.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import kinggora.portal.api.ErrorCode;
import kinggora.portal.api.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class SignInFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    /**
     * 로그인 시도 중
     *
     * @param request   the request during which the authentication attempt occurred.
     * @param response  the response.
     * @param exception the exception which was thrown to reject the authentication
     *                  request.
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.error("Sign In Fail", exception);
        ErrorCode errorCode = getErrorCode(exception);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String json = objectMapper.writeValueAsString(ErrorResponse.of(errorCode));
        response.getWriter().write(json);
    }

    private ErrorCode getErrorCode(AuthenticationException ex) {
        if (ex instanceof BadCredentialsException || ex instanceof UsernameNotFoundException) {
            return ErrorCode.INVALID_CREDENTIAL;
        } else if (ex instanceof AccountExpiredException || ex instanceof CredentialsExpiredException) {
            return ErrorCode.EXPIRED_USER;
        } else if (ex instanceof DisabledException) {
            return ErrorCode.DISABLED_USER;
        } else if (ex instanceof LockedException) {
            return ErrorCode.LOCKED_USER;
        } else {
            return ErrorCode.AUTHENTICATION_ERROR;
        }
    }
}
