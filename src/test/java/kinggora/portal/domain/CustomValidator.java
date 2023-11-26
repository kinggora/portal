package kinggora.portal.domain;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class CustomValidator<T> {

    private Validator validator;

    public CustomValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public boolean validate(T target) {
        Set<ConstraintViolation<T>> validate = validator.validate(target);
        return validate.isEmpty();
    }

    public List<String> getErrorMessage(T target) {
        Set<ConstraintViolation<T>> validate = validator.validate(target);
        Iterator<ConstraintViolation<T>> iterator = validate.iterator();
        List<String> messages = new ArrayList<>();
        while (iterator.hasNext()) {
            ConstraintViolation<T> next = iterator.next();
            messages.add(next.getMessage());
        }
        return messages;
    }
}
