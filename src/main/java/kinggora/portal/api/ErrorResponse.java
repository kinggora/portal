package kinggora.portal.api;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorResponse extends ResponseDto {
    private LocalDateTime timestamp = LocalDateTime.now();
    private List<ErrorField> errors;
    private ErrorResponse(ErrorCode code) {
        super(code.getCode(), code.getDefaultMessage());
    }

    private ErrorResponse(ErrorCode code, String message) {
        super(code.getCode(), message);
    }

    public ErrorResponse(ErrorCode code, List<ErrorField> errors) {
        super(code.getCode(), code.getDefaultMessage());
        this.errors = errors;
    }

    public static ErrorResponse of(ErrorCode code) {
        return new ErrorResponse(code);
    }

    public static ErrorResponse of(ErrorCode code, String message) {
        return new ErrorResponse(code, message);
    }

    public static ErrorResponse of(ErrorCode code, List<ErrorField> errors) {
        return new ErrorResponse(code, errors);
    }

}
