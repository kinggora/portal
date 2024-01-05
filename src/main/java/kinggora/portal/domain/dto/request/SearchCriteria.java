package kinggora.portal.domain.dto.request;

import kinggora.portal.annotation.DateType;
import lombok.*;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchCriteria {
    private Integer boardId;
    private Integer categoryId;
    private String searchWord;
    @DateType(message = "날짜 형식이 올바르지 않습니다.")
    private String startDate;
    @DateType(message = "날짜 형식이 올바르지 않습니다.")
    private String endDate;

    public void injectBoardId(Integer boardId) {
        this.boardId = boardId;
    }
}
