package kinggora.portal.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PageInfo {
    private int pageNum;    // 현재 페이지 번호
    private int pageSize;   // 페이지 당 데이터 수
    private int totalCount; // 전체 데이터 수
    private int totalPages; // 전체 페이지 수
}
