package kinggora.portal.domain.type;

import lombok.RequiredArgsConstructor;

/**
 * 쿼리 정렬 방향 정의
 * - ASC: 오름차
 * - DESC: 내림차
 */
@RequiredArgsConstructor
public enum OrderDirection implements CodeEnum {
    ASC("ASC"),
    DESC("DESC");

    private final String code;

    @Override
    public String getCode() {
        return this.code;
    }
}
