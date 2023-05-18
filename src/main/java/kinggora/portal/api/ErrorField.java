package kinggora.portal.api;

import lombok.*;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

@Getter @Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorField {

    private String object;
    private String field;
    private Object value;
    private String code;
    private String message;

    public static List<ErrorField> of(BindingResult bindingResult){
        List<ErrorField> errors = new ArrayList<>();
        bindingResult.getFieldErrors().forEach(fieldError -> {
            ErrorField error = ErrorField.builder()
                    .object(fieldError.getObjectName())
                    .field(fieldError.getField())
                    .value(fieldError.getRejectedValue())
                    .code(fieldError.getCode())
                    .message(fieldError.getDefaultMessage())
                    .build();
            errors.add(error);
        });
        return errors;
    }
}
