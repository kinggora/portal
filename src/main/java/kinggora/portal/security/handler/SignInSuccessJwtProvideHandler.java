package kinggora.portal.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import kinggora.portal.api.DataResponse;
import kinggora.portal.domain.dto.response.TokenInfo;
import kinggora.portal.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class SignInSuccessJwtProvideHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    /**
     * UsernamePasswordAuthenticationFilter 에서 로그인 요청(POST)에 대한 Authentication 을 주입 받는다.
     * 이 때, 내부적으로 UserDetailsService.loadUserByUsername 을 호출하여 유저 정보를 받아온다.
     * authentication.principal: User
     * authentication.credentials: password
     *
     * @param request        the request which caused the successful authentication
     * @param response       the response
     * @param authentication the <tt>Authentication</tt> object which was created during
     *                       the authentication process.
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("Sign In Success, username={}", authentication.getName());
        TokenInfo tokenInfo = generateToken(authentication);
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String json = objectMapper.writeValueAsString(DataResponse.of(tokenInfo));
        response.getWriter().write(json);
    }

    private TokenInfo generateToken(Authentication authentication) {
        String subject = authentication.getName();
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        return jwtTokenProvider.generateToken(subject, authorities);
    }
}
