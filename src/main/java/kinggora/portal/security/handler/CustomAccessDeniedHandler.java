package kinggora.portal.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import kinggora.portal.api.ErrorCode;
import kinggora.portal.api.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.error("CustomAccessDeniedHandler.handle", accessDeniedException);
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED_ACCESS;
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String json = objectMapper.writeValueAsString(ErrorResponse.of(errorCode));
        response.getWriter().write(json);
    }
}
