package kinggora.portal.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import kinggora.portal.api.ErrorCode;
import kinggora.portal.api.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtExceptionHandlingFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            setErrorResponse(response, e);
        }
    }

    private void setErrorResponse(HttpServletResponse response, Exception exception) {
        ErrorCode errorCode = getErrorCode(exception);
        response.setStatus(errorCode.getStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            String json = objectMapper.writeValueAsString(ErrorResponse.of(errorCode));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error("JwtExceptionHandlingFilter.setErrorResponse", e);
        }
    }

    private ErrorCode getErrorCode(Exception e) {
        if (e instanceof MalformedJwtException) {
            log.error("Invalid JWT Token", e);
            return ErrorCode.INVALID_AUTH_TOKEN;
        } else if (e instanceof ExpiredJwtException) {
            log.error("Expired JWT Token", e);
            return ErrorCode.EXPIRED_AUTH_TOKEN;
        } else if (e instanceof UnsupportedJwtException) {
            log.error("Unsupported JWT Token", e);
            return ErrorCode.UNSUPPORTED_AUTH_TOKEN;
        } else if (e instanceof IllegalArgumentException) {
            log.error("Empty JWT Token", e);
            return ErrorCode.EMPTY_AUTH_TOKEN;
        } else if (e instanceof UsernameNotFoundException) {
            log.error("Authentication Error", e);
            return ErrorCode.AUTHENTICATION_ERROR;
        } else {
            log.error("Unknown Error", e);
            return ErrorCode.INTERNAL_SERVER_ERROR;
        }
    }
}
