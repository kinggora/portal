package kinggora.portal.security.filter;

import kinggora.portal.controller.api.error.ErrorCode;
import kinggora.portal.exception.JwtAuthException;
import kinggora.portal.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JSON Web Token 인증 필터
 * HTTP 요청에 대한 JWT 유효성 검증, 인증 처리
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;

    /**
     * JWT 인증 수행
     * Request Header 에서 JWT 를 추출하고 유효성 검사
     * 토큰이 유효할 경우, 토큰으로부터 username 추출 -> Authentication 객체 생성하여 SecurityContext 에 저장
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);
        if (token != null) {
            jwtProvider.validateToken(token);
            String username = jwtProvider.extractUsername(token);
            Authentication authentication = getAuthentication(username);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    /**
     * HTTP Reqeust 객체로부터 JWT 추출
     * Authorization 헤더의 접두사로 인증 방식(Bearer)을 확인하고 토큰 부분을 반환
     * 헤더 값이 다음과 같을 경우, null 반환
     * - null이거나 빈 문자열
     * - 인증 방식이 Bearer가 아님
     *
     * @param request JWT 추출을 위한 HTTP Request 객체
     * @return JWT 또는 null
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * SecurityContext에 저장할 Authentication 생성
     * 1. username으로 UserDetail 조회
     * 2. Authentication의 구현체인 UsernamePasswordAuthenticationToken 생성하여 반환
     *
     * @param username 사용자 식별 데이터
     * @return UsernamePasswordAuthenticationToken
     * @throws JwtAuthException UserDetail 조회 실패 시 발생
     */
    private Authentication getAuthentication(String username) {
        UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException ex) {
            throw new JwtAuthException(ErrorCode.AUTHENTICATION_ERROR, ex);
        }
        return UsernamePasswordAuthenticationToken.authenticated(userDetails, "", userDetails.getAuthorities());
    }
}
