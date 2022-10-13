package upgrade.campsite_reservation.validators;

import java.time.Period;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import upgrade.campsite_reservation.dto.ReservationDto;

public class MaximumStayValidator implements ConstraintValidator<MaximumStay, ReservationDto> {
	@Override
    public void initialize(MaximumStay maximumStay) {
    }

    @Override
    public boolean isValid(ReservationDto dto, ConstraintValidatorContext cxt) {
    	return Period.between(dto.getStartDate(), dto.getEndDate()).getDays() <= 3;
    }
}
