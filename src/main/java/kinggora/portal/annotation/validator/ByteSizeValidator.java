package kinggora.portal.annotation.validator;

import kinggora.portal.annotation.ByteSize;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.nio.charset.StandardCharsets;

/**
 * Bean Validation Annotation @ByteSize 의 ConstraintValidator
 * ByteSize 애노테이션이 달린 String 타입 필드를 byte array로 변환하고 길이 검증
 */
public class ByteSizeValidator implements ConstraintValidator<ByteSize, String> {

    private int min;
    private int max;

    /**
     * 제약조건 애노테이션 @ByteSize 로부터 속성을 받아와 유효성 검증에 필요한 필드를 초기화
     *
     * @param constraintAnnotation @ByteSize
     */
    @Override
    public void initialize(ByteSize constraintAnnotation) {
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
    }

    /**
     * 실제 검증 수행
     * 문자열 value를 byte array로 변환하고 length 가 min 이상 max 이하인지 검증
     *
     * @param value   검증 값 (문자열)
     * @param context context in which the constraint is evaluated
     * @return
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        return bytes.length >= min && bytes.length <= max;
    }
}
