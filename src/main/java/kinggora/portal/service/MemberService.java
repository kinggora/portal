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
        if(checkDuplicateUsername(member.getUsername())) {
            throw new RuntimeException("중복된 아이디 입니다.");
        }
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        member.setRole(MemberRole.USER);
        return memberRepository.saveMember(member);
    }

    /**
     * 회원 단건 조회 (id)
     * @param id 회원 id
     * @return 회원
     */
    public Member findMemberById(Integer id) {
        return memberRepository.findMemberById(id)
                .orElseThrow(() -> new RuntimeException("fail MemberService.findMemberById"));
    }

    /**
     * 회원 단건 조회 (username)
     * @param username 회원 로그인 id
     * @return 회원
     */
    public Member findMemberByUsername(String username) {
        return memberRepository.findMemberByUsername(username)
                .orElseThrow(() -> new RuntimeException("fail MemberService.findMemberByUsername"));
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

    /**
     * 로그인 아이디 중복 검사
     * @param username 회원 로그인 id
     * @return true: 중복O / false: 중복X
     */
    public boolean checkDuplicateUsername(String username) {
        return memberRepository.checkDuplicateUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findMemberByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return new CustomUserDetails(member);
    }

}
