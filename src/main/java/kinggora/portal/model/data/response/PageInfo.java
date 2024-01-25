package kinggora.portal.model.data.response;

import lombok.*;

/**
 * 페이징 정보 Response Object
 * 페이징 처리를 위한 메타 데이터
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PageInfo {
    private int pageNum;    // 현재 페이지 번호
    private int pageSize;   // 페이지 당 데이터 수
    private int totalCount; // 전체 데이터 수
    private int totalPages; // 전체 페이지 수
}
