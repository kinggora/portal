package kinggora.portal.web.validation.validator;

import kinggora.portal.web.validation.PeriodType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Bean Validation Annotation @PeriodType 의 ConstraintValidator
 * PeriodType
 */
@Slf4j
public class PeriodTypeValidator implements ConstraintValidator<PeriodType, Object> {

    private String previousPropertyName;
    private String laterPropertyName;

    /**
     * 제약조건 애노테이션 @PeriodType 로부터 속성을 받아와 유효성 검증에 필요한 필드를 초기화
     *
     * @param constraintAnnotation @PeriodType
     */
    @Override
    public void initialize(PeriodType constraintAnnotation) {
        previousPropertyName = constraintAnnotation.previous();
        laterPropertyName = constraintAnnotation.later();
    }

    /**
     * 실제 검증 수행
     * 객체 value로부터 프로퍼티명으로 검증할 프로퍼티(previousValue, laterValue)를 가져옴
     * previousValue와 laterValue가 검증 대상인지 확인 -> 검증 대상이 아닐 시 true 반환
     * 검증 실패 시 검증 실패한 프로퍼티 정보 등록
     *
     * @param value   검증 값 (객체)
     * @param context context in which the constraint is evaluated
     * @return 기간 유효성 검증 결과
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        BeanWrapperImpl wrapper = new BeanWrapperImpl(value);
        Object previousValue = wrapper.getPropertyValue(previousPropertyName);
        Object laterValue = wrapper.getPropertyValue(laterPropertyName);
        boolean previousIsSupported = isSupported(previousValue);
        boolean laterIsSupported = isSupported(laterValue);
        if (!(previousIsSupported && laterIsSupported)) {
            return true;
        }
        LocalDate previousDate = LocalDate.parse((String) previousValue);
        LocalDate laterDate = LocalDate.parse((String) laterValue);
        boolean result = validatePeriod(previousDate, laterDate);
        if (!result) {
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode(previousPropertyName)
                    .addConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode(laterPropertyName)
                    .addConstraintViolation();
        }
        return result;
    }

    /**
     * LocalDate 기간 유효성 검증
     *
     * @param previousDate
     * @param laterDate
     * @return true: previousDate <= laterDate, false: previousDate > laterDate
     */
    private boolean validatePeriod(LocalDate previousDate, LocalDate laterDate) {
        return previousDate.equals(laterDate) || previousDate.isBefore(laterDate);
    }

    /**
     * Object가 검증 대상인지 확인
     * 1. null이 아닌 String 타입
     * 2. LocalDate 타입으로 파싱 가능
     *
     * @param target
     * @return
     */
    private boolean isSupported(Object target) {
        if (!(target instanceof String)) {
            return false;
        }
        try {
            LocalDate.parse((String) target);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

}
