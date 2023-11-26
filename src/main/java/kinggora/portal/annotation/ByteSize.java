package kinggora.portal.annotation;

import kinggora.portal.annotation.validator.ByteSizeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = ByteSizeValidator.class)
public @interface ByteSize {

    String message() default "";

    /**
     * @return byte size the element must be higher or equal to
     */
    int min() default 0;

    /**
     * @return byte size the element must be lower or equal to
     */
    int max() default Integer.MAX_VALUE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
