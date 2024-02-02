package kinggora.portal.security.user;

import kinggora.portal.domain.Member;
import kinggora.portal.domain.type.MemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Spring Security User
 * Authentication 객체로 캡슐화 되어 사용자 인증에 사용
 * DB에 저장된 회원 정보(member) 기반으로 UserDetails 인터페이스 구현
 */
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final Member member;

    /**
     * 사용자 권한 반환
     *
     * @return NOT NULL authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> auth = new ArrayList<>();
        for (MemberRole role : member.getRoles()) {
            auth.add(new SimpleGrantedAuthority(role.getCode()));
        }
        return auth;
    }

    /**
     * 계정 인증을 위한 password 반환
     *
     * @return member.password
     */
    @Override
    public String getPassword() {
        return member.getPassword();
    }

    /**
     * 계정 인증을 위한 username 반환
     *
     * @return NOT NULL member.username
     */
    @Override
    public String getUsername() {
        return member.getUsername();
    }

    /**
     * 계정 만료 여부
     *
     * @return true: 만료되지 않음, false: 만료됨
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 계정 잠김 여부
     *
     * @return true: 잠기지 않음, false: 잠김
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Credentials 만료 여부
     *
     * @return true: 만료되지 않음, false: 만료됨
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 계정 활성화 여부
     * 탈퇴 여부(member.deleted)에 따라 탈퇴한 회원은 계정 비활성화
     *
     * @return true: 활성화, false: 비활성화
     */
    @Override
    public boolean isEnabled() {
        return !member.isDeleted();
    }

    public Member getMember() {
        return member;
    }

    public Integer getId() {
        return getMember().getId();
    }

    public boolean isAdmin() {
        return getAuthorities().stream()
                .anyMatch(authority ->
                        MemberRole.ADMIN.getCode().equals(authority.getAuthority()));
    }
}
