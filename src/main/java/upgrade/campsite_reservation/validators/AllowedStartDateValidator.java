package upgrade.campsite_reservation.validators;

import java.time.LocalDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import upgrade.campsite_reservation.dto.ReservationDto;

public class AllowedStartDateValidator implements ConstraintValidator<AllowedStartDate, ReservationDto> {

    @Override
    public void initialize(AllowedStartDate allowedStartDate) {
    }

    @Override
    public boolean isValid(ReservationDto dto, ConstraintValidatorContext cxt) {
    	return LocalDate.now().isBefore(dto.getStartDate()) && dto.getStartDate().isBefore(LocalDate.now().plusMonths(1).plusDays(1));
    }
    
}
