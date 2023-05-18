package kinggora.portal.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class ResponseDto {
    private int code;
    private String message;
}
