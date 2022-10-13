package upgrade.campsite_reservation.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = StartDateBeforeEndDateValidator.class)
@Target( { ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface StartDateBeforeEndDate {
	String message() default "Reservation stay cannot be longer than 3 days";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
