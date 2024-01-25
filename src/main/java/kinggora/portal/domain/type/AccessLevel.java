package kinggora.portal.domain.type;

import lombok.RequiredArgsConstructor;

/**
 * 게시판 요청에 대한 접근 수준 정의
 * - ALL: 로그인 여부와 관계 없이 모든 접근 허용
 * - USER: 로그인 사용자 허용
 * - ADMIN: 관리자 허용
 * - NONE: 모든 접근 비허용
 */
@RequiredArgsConstructor
public enum AccessLevel implements CodeEnum {
    ALL("ALL"),
    USER("USER"),
    ADMIN("ADMIN"),
    NONE("NONE");
    private final String code;

    @Override
    public String getCode() {
        return this.code;
    }
}
