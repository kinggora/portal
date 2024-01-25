package kinggora.portal.security.user;

import kinggora.portal.domain.Member;
import kinggora.portal.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Spring Security User Service
 * Member DAO로 Spring Security 프레임워크 전반에서 사용
 * DaoAuthenticationProvider에서 UserDetails 로드에 사용
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;

    /**
     * DB에서 회원 정보를 조회하고 UserDetails 생성하여 반환
     *
     * @param username 사용자 식별 데이터 (member.username)
     * @return NOT NULL UserDetails
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return new CustomUserDetails(member);
    }
}
