package kinggora.portal.api;

import lombok.Getter;

@Getter
public class DataResponse<T> extends ResponseDto{

    private final T data;

    private DataResponse(T data) {
        super(200, "OK");
        this.data = data;
    }

    private DataResponse(String massage, T data) {
        super(200, massage);
        this.data = data;
    }

    public static <T> DataResponse<T> of(T data) {
        return new DataResponse<>(data);
    }

    public static <T> DataResponse<T> of(String message, T data) {
        return new DataResponse<>(message, data);
    }

    public static <T> DataResponse<T> empty() {
        return new DataResponse<>(null);
    }
}
