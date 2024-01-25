package kinggora.portal.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import kinggora.portal.controller.api.error.ErrorCode;
import kinggora.portal.controller.api.error.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Spring Security 필터 단에서 발생한 AuthenticationException 핸들러
 * 예외 응답을 JSON 형태로 반환
 */
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    /**
     * AuthenticationException 예외 처리
     * ObjectMapper를 통해 에러 응답 객체를 직렬화하여 반환
     * permitAll()이 아닐 때 익명 사용자, remember-me 사용자에 의한 접근 거부도 처리
     * HTTP Status: 401 (Unauthorized)
     *
     * @param request   AuthenticationException을 발생시킨 HTTP Request
     * @param response  사용자에게 요청 실패 응답을 반환하기 위한 HTTP Response
     * @param exception 핸들러 호출 원인
     */
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        log.error("CustomAuthenticationEntryPoint.commence", exception);
        ErrorCode errorCode = ErrorCode.AUTHENTICATION_ERROR;
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            String json = objectMapper.writeValueAsString(ErrorResponse.of(errorCode));
            response.getWriter().write(json);
        } catch (IOException e) {
            log.error("CustomAuthenticationEntryPoint.commence", e);
        }
    }
}
