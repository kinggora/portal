package kinggora.portal.domain.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MemberRole implements CodeEnum {
    USER("ROLE_USER"), // 로그인 이용자 권한
    ADMIN("ROLE_ADMIN"); // 관리자 권한

    private final String code;

    @Override
    public String getCode() {
        return this.code;
    }
}
