package kinggora.portal.domain.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Getter
@RequiredArgsConstructor
public class Id {

    private final int id;

    public static Id from(String id) {
        validateNull(id);
        int intValue = parseId(id);
        validatePositive(intValue);
        return new Id(intValue);
    }

    private static void validatePositive(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("id 값은 양의 정수만 가능합니다.");
        }
    }

    private static void validateNull(String id) {
        if (Objects.isNull(id) || id.isBlank()) {
            throw new IllegalArgumentException("id 는 빈 값이 될 수 없습니다.");
        }
    }

    private static int parseId(String id) {
        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("id 값은 숫자만 가능합니다.");
        }
    }

}
