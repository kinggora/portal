package kinggora.portal.security.user;

import kinggora.portal.domain.Member;
import kinggora.portal.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    /**
     * @param username 사용자 식별 데이터 (member.username)
     * @return Authentication 의 Principal
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        Member member = memberRepository.findMemberByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return new CustomUserDetails(member);
    }
}
