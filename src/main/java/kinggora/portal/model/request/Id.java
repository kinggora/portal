package kinggora.portal.model.request;

import lombok.Getter;

import java.util.Objects;

/**
 * HTTP 요청 파라미터를 변환하기 위한 타입
 * DAO에서 도메인 식별자(id)로 사용될 수 있는지 파라미터 검증 후에 객체 생성
 */
@Getter
public class Id {

    private final int id;

    /**
     * private 생성자
     * 객체 생성을 from() 메서드에 위임
     *
     * @param id
     */
    private Id(int id) {
        this.id = id;
    }

    /**
     * 입력 값을 검증하고 Id 객체를 생성하는 정적 팩토리 메서드
     *
     * @param id 입력 값
     * @return Id
     */
    public static Id from(String id) {
        validateNull(id);
        int intValue = parseId(id);
        validatePositive(intValue);
        return new Id(intValue);
    }

    /**
     * 문자열이 null 이거나 빈 문자열, 공백 문자열인지 검증
     *
     * @param id 입력 문자열
     */
    private static void validateNull(String id) {
        if (Objects.isNull(id) || id.isBlank()) {
            throw new IllegalArgumentException("id 는 빈 값이 될 수 없습니다.");
        }
    }

    /**
     * String 타입을 int 타입으로 변환
     *
     * @param id String 타입 입력 값
     * @return int 타입 입력 값
     */
    private static int parseId(String id) {
        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("id 값은 숫자만 가능합니다.");
        }
    }

    /**
     * 양의 정수인지 검증
     *
     * @param id int 타입 입력 값
     */
    private static void validatePositive(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("id 값은 양의 정수만 가능합니다.");
        }
    }

}
