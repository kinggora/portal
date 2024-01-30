package kinggora.portal.model.data.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 페이징 정보 Response Object
 * 페이징 처리를 위한 메타 데이터
 */
@Getter
@Builder
@AllArgsConstructor
public class PageInfo {
    private int pageNum;
    private int pageSize;
    private int totalCount;
    private int totalPages;

    private PageInfo() {
    }

    /**
     * 페이징 메타 데이터(PageInfo) 생성
     * totalPage: totalCount와 pageSize를 이용하여 계산
     *
     * @param totalCount 전체 데이터 수
     * @param pageNum    현재 페이지 번호
     * @param pageSize   페이지 당 데이터 수
     * @return PageInfo
     */
    public static PageInfo create(int totalCount, int pageNum, int pageSize) {
        int totalPages = totalCount == 0 ? 1 : (totalCount - 1) / pageSize + 1;
        return PageInfo.builder()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .totalCount(totalCount)
                .totalPages(totalPages)
                .build();
    }
}
