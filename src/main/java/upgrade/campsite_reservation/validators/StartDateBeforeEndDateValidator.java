package upgrade.campsite_reservation.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import upgrade.campsite_reservation.dto.ReservationDto;

public class StartDateBeforeEndDateValidator implements ConstraintValidator<StartDateBeforeEndDate, ReservationDto> {
	@Override
    public void initialize(StartDateBeforeEndDate startDateBeforeEndDate) {
    }

    @Override
    public boolean isValid(ReservationDto dto, ConstraintValidatorContext cxt) {
    	return dto.getStartDate().isBefore(dto.getEndDate());
    }
}
