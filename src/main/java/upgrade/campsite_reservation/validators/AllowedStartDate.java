package upgrade.campsite_reservation.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = AllowedStartDateValidator.class)
@Target( { ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowedStartDate {
	String message() default "Reservation start date must be from 1 day ahead of arrival and up to 1 month in advance";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
