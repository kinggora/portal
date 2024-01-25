package kinggora.portal.model.request;

import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.Objects;

/**
 * 페이징 요청 파라미터
 * - 컨트롤러 단에서 @ModelAttribute 로 바인딩되어 Bean Validation 수행
 * - GET 쿼리 파라미터 바인딩
 * - 각 필드에 대한 default value 정의
 */
@Getter
public class PagingCriteria {
    private final static int DEFAULT_PAGE_VALUE = 1;
    private final static int DEFAULT_SIZE_SIZE = 10;
    @Positive
    private final int page;
    @Min(value = 1)
    @Max(value = 40)
    private final int size;

    /**
     * 파라미터 주입을 위한 생성자
     * null 주입시 기본 값으로 초기화
     *
     * @param page 페이지 넘버
     * @param size 페이지 크기
     */
    public PagingCriteria(Integer page, Integer size) {
        this.page = Objects.requireNonNullElse(page, DEFAULT_PAGE_VALUE);
        this.size = Objects.requireNonNullElse(size, DEFAULT_SIZE_SIZE);
    }
}
