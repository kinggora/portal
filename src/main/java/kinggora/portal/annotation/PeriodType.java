package kinggora.portal.annotation;

import kinggora.portal.annotation.validator.PeriodTypeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Bean Validation Annotation
 */
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = PeriodTypeValidator.class)
@Documented
public @interface PeriodType {

    String message() default "유효한 기간 타입이 아닙니다.";

    /**
     * @return Period을 이루는 성분 중 이전 날짜
     */
    String previous();

    /**
     * @return Period을 이루는 성분 중 이후 날짜
     */
    String later();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
