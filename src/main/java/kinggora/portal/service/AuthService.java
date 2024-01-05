package kinggora.portal.service;

import kinggora.portal.domain.Member;
import kinggora.portal.domain.dto.response.TokenInfo;
import kinggora.portal.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;


    /**
     * 회원 로그인
     * UserDetailsService.loadUserByUsername() 실행하여 회원 검증
     *
     * @param member 로그인 요청 데이터 (username, password)
     * @return JWT 토큰
     */
    public TokenInfo signIn(Member member) {
        log.info("member id={}, password={}", member.getUsername(), member.getPassword());
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(member.getUsername(), member.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return jwtTokenProvider.generateToken(authentication);
    }

}
