package kinggora.portal.domain.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {
    USER("ROLE_USER"), //회원가입 O
    ADMIN("ROLE_ADMIN"), //관리자
    GUEST("ROLE_GUEST"); //회원가입 X

    private final String value;
}
