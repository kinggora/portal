package kinggora.portal.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccessLevel {
    ALL("ALL"), // 로그인 여부와 관계 없이 모든 접근 허용
    USER("USER"), // 로그인 사용자(MemberRole.USER) 허용
    ADMIN("ADMIN"), // 관리자(MemberRole.ADMIN) 허용
    NONE("NONE"); // 모든 접근 비허용

    private final String value;
}
