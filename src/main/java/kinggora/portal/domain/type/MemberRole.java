package kinggora.portal.domain.type;

import lombok.RequiredArgsConstructor;

/**
 * 회원 권한 정의
 * - USER: 로그인 이용자 권한
 * - ADMIN: 관리자 권한
 */
@RequiredArgsConstructor
public enum MemberRole implements CodeEnum {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final String code;

    @Override
    public String getCode() {
        return this.code;
    }
}
