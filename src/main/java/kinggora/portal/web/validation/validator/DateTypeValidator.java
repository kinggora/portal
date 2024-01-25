package kinggora.portal.web.validation.validator;

import kinggora.portal.web.validation.DateType;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Bean Validation Annotation @DateType 의 ConstraintValidator
 * DateType 애노테이션이 달린 String 타입 필드가 날짜 타입으로 파싱 가능한지, 유효한 날짜인지 확인
 */
public class DateTypeValidator implements ConstraintValidator<DateType, String> {

    private static final LocalDate MINIMUM_LOCAL_DATE = LocalDate.of(2017, 1, 1);
    private static final LocalDate MAXIMUM_LOCAL_DATE = LocalDate.now();
    private static final String DATE_FORMAT = "yyyy-MM-dd";


    /**
     * 제약조건 애노테이션 @DateType 로부터 속성을 받아와 유효성 검증에 필요한 필드를 초기화
     *
     * @param constraintAnnotation @DateType
     */
    @Override
    public void initialize(DateType constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * 실제 검증 수행
     * 1. value가 LocalDate 타입으로 파싱 가능한 문자열인지 검증
     * 2. value가 유효한 범위의 값인지 검증
     *
     * @param value   검증 값
     * @param context context in which the constraint is evaluated
     * @return 검증 결과
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        HibernateConstraintValidatorContext hibernateContext = context.unwrap(HibernateConstraintValidatorContext.class);
        hibernateContext.disableDefaultConstraintViolation();
        LocalDate date;
        try {
            date = LocalDate.parse(value, DateTimeFormatter.ofPattern(DATE_FORMAT));
        } catch (DateTimeParseException e) {
            hibernateContext
                    .addMessageParameter("pattern", DATE_FORMAT)
                    .buildConstraintViolationWithTemplate("{constraint.date-type.format}")
                    .addConstraintViolation();
            return false;
        }

        boolean isValid = validateRange(date);
        if (!isValid) {
            hibernateContext
                    .addMessageParameter("from", MINIMUM_LOCAL_DATE)
                    .addMessageParameter("to", MAXIMUM_LOCAL_DATE)
                    .buildConstraintViolationWithTemplate("{constraint.date-type.range}")
                    .addConstraintViolation();
        }
        return isValid;
    }

    /**
     * date 유효성 검증
     * MINIMUM_LOCAL_DATE <= date && date <= MAXIMUM_LOCAL_DATE 인지 확인
     *
     * @param date 검증할 날짜
     * @return 검증 결과
     */
    private boolean validateRange(LocalDate date) {
        return (date.isEqual(MINIMUM_LOCAL_DATE) || date.isAfter(MINIMUM_LOCAL_DATE))
                && (date.isEqual(MAXIMUM_LOCAL_DATE) || date.isBefore(MAXIMUM_LOCAL_DATE));
    }

}
