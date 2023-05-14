package kinggora.portal.service;

import kinggora.portal.domain.Member;
import kinggora.portal.domain.dto.MemberRole;
import kinggora.portal.domain.dto.TokenInfo;
import kinggora.portal.repository.MemberRepository;
import kinggora.portal.security.CustomUserDetails;
import kinggora.portal.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    /**
     * 회원 등록
     * 로그인 아이디 중복 시 RuntimeException 발생
     * @param member
     * @return 등록 회원 id
     */
    public Integer register(Member member) {
        if(memberRepository.checkDuplicateUsername(member.getUsername())) {
            throw new RuntimeException("중복된 아이디 입니다.");
        }
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        member.setRole(MemberRole.USER);
        return memberRepository.saveMember(member);
    }

    /**
     * 회원 로그인
     * UserDetailsService.loadUserByUsername() 실행하여 회원 검증
     * @param member
     * @return JWT 토큰
     */
    public TokenInfo signIn(Member member) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(member.getUsername(), member.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return jwtTokenProvider.generateToken(authentication);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return new CustomUserDetails(member);
    }

}
