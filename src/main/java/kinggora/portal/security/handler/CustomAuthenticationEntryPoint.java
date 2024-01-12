package kinggora.portal.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import kinggora.portal.api.ErrorCode;
import kinggora.portal.api.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    /**
     * 인증 실패 시 AuthenticationException 처리
     * permitAll()이 아닐 때 익명 사용자, remember-me 사용자에 의한 접근 거부도 처리한다.
     *
     * @throws IOException
     * @throws ServletException
     */
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.error("CustomAuthenticationEntryPoint.commence", exception);
        ErrorCode errorCode = ErrorCode.AUTHENTICATION_ERROR;
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String json = objectMapper.writeValueAsString(ErrorResponse.of(errorCode));
        response.getWriter().write(json);
    }
}
