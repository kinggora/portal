package kinggora.portal.security.filter;

import kinggora.portal.exception.IllegalAuthArgumentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 로그인 인증 처리 필터
 * 인증에 필요한 파라미터(username, password)를 HTTP Request로부터 추출하기 위한 Key와
 * 인증 요청에 대한 AntPathRequestMatcher를 static 필드으로 정의
 */
@Slf4j
public class CustomUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";
    private static final AntPathRequestMatcher DEFAULT_ANT_REQUEST_MATCHER = new AntPathRequestMatcher("/signin", "POST");

    /**
     * AbstractAuthenticationProcessingFilter의 생성자 호출하여 다음 두 객체를 초기화
     * - RequestMatcher: 인증 요청의 URL, HTTP Method 정의
     * - AuthenticationManager: Authentication 객체 인증을 수행하는 객체
     *
     * @param authenticationManager AuthenticationManager 구현체
     */
    public CustomUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(DEFAULT_ANT_REQUEST_MATCHER, authenticationManager);
    }

    /**
     * 실제 인증을 수행하는 메서드
     * HTTP Request 로부터 username, password를 추출하여 인증 토큰 생성
     * AuthenticationManager.authenticate(token)의 결과로 인증이 끝난 Authentication 객체 반환
     *
     * @param request  인증 요청 HTTP Request
     * @param response 인증 처리 중 필요할 수 있는 HTTP Response 객체
     * @return 인증된 Authentication 또는 인증이 완료되지 않은 경우 null
     * @throws AuthenticationException 인증 실패 시 발생
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = extractUsername(request);
        String password = extractPassword(request);
        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    /**
     * HTTP Request에서 username 추출
     *
     * @param request 인증 파라미터 추출을 위한 HTTP Request 객체
     * @return username 파라미터
     */
    private String extractUsername(HttpServletRequest request) {
        String parameter = request.getParameter(USERNAME_KEY);
        if (!StringUtils.hasText(parameter)) {
            throw new IllegalAuthArgumentException("Username 파라미터는 null 이나 empty string 일 수 없습니다.");
        }
        return parameter;
    }

    /**
     * HTTP Request에서 password 추출
     *
     * @param request 인증 파라미터 추출을 위한 HTTP Request 객체
     * @return password 파라미터
     */
    private String extractPassword(HttpServletRequest request) {
        String parameter = request.getParameter(PASSWORD_KEY);
        if (!StringUtils.hasText(parameter)) {
            throw new IllegalAuthArgumentException("Password 파라미터는 null 이나 empty string 일 수 없습니다.");
        }
        return parameter;
    }
}
