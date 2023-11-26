package kinggora.portal.annotation.validator;

import kinggora.portal.annotation.ByteSize;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.UnsupportedEncodingException;

@Slf4j
public class ByteSizeValidator implements ConstraintValidator<ByteSize, String> {

    private int min;
    private int max;

    @Override
    public void initialize(ByteSize constraintAnnotation) {
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.length() == 0) {
            return false;
        }
        byte[] bytes;
        try {
            bytes = value.getBytes("EUC-KR");
        } catch (UnsupportedEncodingException e) {
            log.error("ByteSizeValidator.isValid", e);
            return false;
        }
        return bytes.length >= min && bytes.length <= max;
    }
}
