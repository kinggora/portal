package kinggora.portal.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import kinggora.portal.controller.api.error.ErrorResponse;
import kinggora.portal.exception.JwtAuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JwtAuthenticationFilter에서 발생한 JwtAuthException 핸들러 필터
 * 예외 응답을 JSON 형태로 반환
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthExceptionHandlingFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    /**
     * 다음 필터를 호출하고 JwtAuthException 발생 시 예외 처리
     *
     * @param request     JwtAuthException을 발생시킨 HTTP Request
     * @param response    사용자에게 요청 실패 응답을 반환하기 위한 HTTP Response
     * @param filterChain
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JwtAuthException e) {
            resolveException(response, e);
        }
    }

    /**
     * JwtAuthException 예외 처리
     * ObjectMapper를 통해 에러 응답 객체를 직렬화하여 반환
     * HTTP Status: 401 (Unauthorized)
     *
     * @param response 사용자에게 요청 실패 응답을 반환하기 위한 HTTP Response
     * @param ex       JWT 인증 실패 예외
     */
    private void resolveException(HttpServletResponse response, JwtAuthException ex) {
        log.error("JwtExceptionHandlingFilter.resolveException", ex);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            String json = objectMapper.writeValueAsString(ErrorResponse.of(ex.getCode()));
            response.getWriter().write(json);
        } catch (IOException e) {
            log.error("JwtExceptionHandlingFilter.resolveException", e);
        }
    }
}
