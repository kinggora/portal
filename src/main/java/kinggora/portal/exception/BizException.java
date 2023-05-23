package kinggora.portal.exception;

import kinggora.portal.api.ErrorCode;
import lombok.Getter;

@Getter
public class BizException extends RuntimeException{

    private final ErrorCode code;

    public BizException(ErrorCode code){
        this.code = code;
    }

    public BizException(ErrorCode code, String message){
        super(message);
        this.code = code;
    }
}
