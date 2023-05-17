package kinggora.portal.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SearchCriteria {
    private Integer page = 1;
    private Integer categoryId;
    private String searchWord;
    private String startDate;
    private String endDate;
}
