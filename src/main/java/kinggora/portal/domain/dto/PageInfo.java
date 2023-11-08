package kinggora.portal.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PageInfo {
    private int pageNum;
    private int pageSize;
    private int totalCount;
    private int totalPages;
}
