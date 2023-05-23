package kinggora.portal.domain.dto;

import lombok.*;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchCriteria {
    private Integer page = 1;
    private Integer categoryId;
    private String searchWord;
    private String startDate;
    private String endDate;
}
