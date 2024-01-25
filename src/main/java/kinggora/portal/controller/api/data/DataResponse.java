package kinggora.portal.controller.api.data;

import kinggora.portal.controller.api.ApiResponse;
import lombok.Getter;

/**
 * 요청 성공 시 반환하는 API Response Object
 *
 * @param <T> data의 타입
 */
@Getter
public class DataResponse<T> extends ApiResponse {

    private final T data;

    protected DataResponse(T data) {
        super(200, "OK");
        this.data = data;
    }

    /**
     * 데이터를 포함한 DataResponse 객체 생성
     *
     * @param data 요청에 대한 데이터
     * @param <T>  data의 타입
     * @return DataResponse
     */
    public static <T> DataResponse<T> of(T data) {
        return new DataResponse<>(data);
    }

    /**
     * 데이터를 포함하지 않은 DataResponse 객체 생성
     *
     * @return DataResponse
     */
    public static DataResponse<Void> empty() {
        return new DataResponse<>(null);
    }
}
