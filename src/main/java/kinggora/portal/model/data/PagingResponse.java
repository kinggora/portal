package kinggora.portal.model.data;

import kinggora.portal.model.ApiResponse;
import kinggora.portal.model.data.response.PageInfo;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 요청 성공 시 반환하는 API Response Object
 * 목록 조회 요청에서 페이징 정보가 포함된 응답에 사용
 *
 * @param <T> data 타입
 */
@Getter
@Builder
public class PagingResponse<T> extends ApiResponse {
    private final List<T> data;
    private final PageInfo pageInfo;

    public PagingResponse(List<T> data, PageInfo pageInfo) {
        super(200, "OK");
        this.data = data;
        this.pageInfo = pageInfo;
    }
}
