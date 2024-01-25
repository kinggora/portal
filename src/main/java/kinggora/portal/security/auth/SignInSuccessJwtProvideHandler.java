package kinggora.portal.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import kinggora.portal.model.data.DataResponse;
import kinggora.portal.model.data.response.TokenInfo;
import kinggora.portal.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * 로그인 인증 성공 핸들러
 * JWT 응답 객체를 생성하여 JSON 형태로 반환
 */
@Slf4j
@RequiredArgsConstructor
public class SignInSuccessJwtProvideHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;

    /**
     * 로그인 인증 성공 응답
     * Authentication 객체로 JWT를 생성하고 ObjectMapper를 통해 직렬화하여 반환
     *
     * @param request        로그인 인증에 성공한 HTTP Request
     * @param response       Response
     * @param authentication 로그인 인증 객체
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        TokenInfo tokenInfo = generateToken(authentication);
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            String json = objectMapper.writeValueAsString(DataResponse.of(tokenInfo));
            response.getWriter().write(json);
        } catch (IOException e) {
            log.error("SignInSuccessJwtProvideHandler.onAuthenticationSuccess", e);
        }
    }

    /**
     * JWT 응답 객체 생성
     * authentication의 username, authorities를 소스로 TokenInfo 생성하여 반환
     *
     * @param authentication 로그인 인증 객체
     * @return TokenInfo
     */
    private TokenInfo generateToken(Authentication authentication) {
        String username = authentication.getName();
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        return jwtProvider.generateToken(username, authorities);
    }
}
