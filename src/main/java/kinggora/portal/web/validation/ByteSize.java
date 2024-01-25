package kinggora.portal.web.validation;

import kinggora.portal.web.validation.validator.ByteSizeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Bean Validation Annotation
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = ByteSizeValidator.class)
public @interface ByteSize {

    String message() default "";

    /**
     * @return 입력 값의 byte size가 가능한 최솟값
     */
    int min() default 0;

    /**
     * @return 입력 값의 byte size가 가능한 최댓값
     */
    int max() default Integer.MAX_VALUE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
