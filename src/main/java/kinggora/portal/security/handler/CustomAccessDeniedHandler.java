package kinggora.portal.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import kinggora.portal.controller.api.error.ErrorCode;
import kinggora.portal.controller.api.error.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Spring Security 필터 단에서 발생한 AccessDeniedException 핸들러
 * 예외 응답을 JSON 형태로 반환
 */
@Slf4j
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    /**
     * AccessDeniedException 예외 처리
     * ObjectMapper를 통해 에러 응답 객체를 직렬화하여 반환
     * HTTP Status: 403 (Forbidden)
     *
     * @param request               AccessDeniedException을 발생시킨 HTTP Request
     * @param response              사용자에게 요청 실패 응답을 반환하기 위한 HTTP Response
     * @param accessDeniedException 핸들러 호출 원인
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
        log.error("CustomAccessDeniedHandler.handle", accessDeniedException);
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED_ACCESS;
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            String json = objectMapper.writeValueAsString(ErrorResponse.of(errorCode));
            response.getWriter().write(json);
        } catch (IOException ex) {
            log.error("CustomAccessDeniedHandler.handle", accessDeniedException);
        }
    }
}
