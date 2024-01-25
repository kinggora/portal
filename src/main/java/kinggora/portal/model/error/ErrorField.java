package kinggora.portal.model.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

/**
 * BindException, MethodArgumentNotValidException 발생 시 바인딩에 실패한 필드 정보
 */
@Getter
@Builder
@ToString
@AllArgsConstructor
@Slf4j
public class ErrorField {

    private String object;
    private String field;
    private Object value;
    private String code;
    private String message;


    /**
     * BindingResult의 FieldError로부터 필요한 정보를 받아와서 ErrorField 객체를 생성
     *
     * @param bindingResult 바인딩 결과
     * @return 바인딩 실패 정보를 담은 ErrorField 리스트
     */
    public static List<ErrorField> of(BindingResult bindingResult) {
        List<ErrorField> errors = new ArrayList<>();
        bindingResult.getGlobalErrors().forEach(objectError -> {
            log.info(objectError.getDefaultMessage());
//            ErrorField error = ErrorField.builder()
//                    .object(objectError.getObjectName())
//                    .field(Arrays.stream().forEach(args);)
//                    .value()
//                    .code(objectError.getCode())
//                    .message(objectError.getDefaultMessage())
//                    .build();
        });
        bindingResult.getFieldErrors().forEach(fieldError -> {
            ErrorField error = ErrorField.builder()
                    .object(fieldError.getObjectName())
                    .field(fieldError.getField())
                    .value(fieldError.getRejectedValue())
                    .code(fieldError.getCode())
                    .message(fieldError.getDefaultMessage())
                    .build();
            errors.add(error);
            log.info("error={}", error);
        });
        return errors;
    }

}
