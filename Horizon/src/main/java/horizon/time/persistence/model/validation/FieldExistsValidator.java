package horizon.time.persistence.model.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FieldExistsValidator implements ConstraintValidator<FieldExists, Object> {

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		// return value != null && value.matches("[0-9]+") && (value.length() > 8) &&
		// (value.length() < 14);
		/*
		 * System.out.println("Validation value:"); System.out.println(value);
		 * System.out.println("Validation context:"); System.out.println(context);
		 */
		return false;
	}

}
