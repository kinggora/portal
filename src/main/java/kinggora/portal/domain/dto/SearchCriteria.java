package kinggora.portal.domain.dto;

import lombok.*;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchCriteria {
    private Integer page = 1;
    private Integer pageSize = 10;
    private Integer boardId;
    private Integer categoryId;
    private String searchWord;
    private String startDate;
    private String endDate;
}
