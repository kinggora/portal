package kinggora.portal.model.data.request;

import kinggora.portal.web.validation.DateType;
import kinggora.portal.web.validation.PeriodType;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.StringUtils;

import javax.validation.constraints.Positive;

/**
 * 게시글 검색 요청 파라미터
 * - 컨트롤러 단에서 @ModelAttribute 로 바인딩되어 Bean Validation 수행
 * - GET 쿼리 파라미터 바인딩
 */
@Getter
@Builder
@PeriodType(previous = "startDate", later = "endDate")
public class BoardSearchParam {

    @Positive
    private final Integer categoryId;
    private final String searchWord;
    @DateType
    private final String startDate;
    @DateType
    private final String endDate;

    /**
     * 파라미터 주입을 위한 생성자
     * 검색어는 null이나 빈 문자열이 아닌 경우, 문자열의 앞 뒤 공백 제거
     *
     * @param categoryId 카테고리 id
     * @param searchWord 검색어
     * @param startDate  시작 날짜
     * @param endDate    종료 날짜
     */
    public BoardSearchParam(Integer categoryId, String searchWord, String startDate, String endDate) {
        this.categoryId = categoryId;
        this.searchWord = StringUtils.hasLength(searchWord) ? searchWord.strip() : searchWord;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
