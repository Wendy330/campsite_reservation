package upgrade.campsite_reservation.validators;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import upgrade.campsite_reservation.dto.ReservationDto;

public class ReservationValidatorTest {
	ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	Validator validator;
	LocalDate now;
	ReservationDto reservationDto;
	Set<ConstraintViolation<ReservationDto>> violations;

	@BeforeEach
	void beforeEach() {
		validator = factory.getValidator();
		now = LocalDate.now();
		reservationDto = null;
		violations = null;
	}

	@Nested
	public class AllowedStartDateValidator {

		@Test
		public void whenStartDateIsOneDayAhead_thenNoValidationErrorThrown() {
			reservationDto = buildReservationDtoWithDates(now.plusDays(1), now.plusDays(2));

			violations = validator.validate(reservationDto);

			assertThat(violations.size()).isZero();
		}

		@Test
		public void whenStartDateIsOneMonthAhead_thenNoValidationErrorThrown() {
			reservationDto = buildReservationDtoWithDates(now.plusMonths(1), now.plusMonths(1).plusDays(2));

			violations = validator.validate(reservationDto);

			assertThat(violations.size()).isZero();
		}

		@Test
		public void whenStartDateIsInThePast_thenThrowsAllowedStartDateException() {
			reservationDto = buildReservationDtoWithDates(now.minusDays(2), now);

			violations = validator.validate(reservationDto);

			assertThat(violations.size()).isEqualTo(1);
			violations.forEach(
					v -> assertThat(v.getConstraintDescriptor().getAnnotation().annotationType().getCanonicalName())
							.isEqualTo(AllowedStartDate.class.getCanonicalName()));
		}

		@Test
		public void whenStartDateIsMoreThanOneMonth_thenThrowsAllowedStartDateException() {
			reservationDto = buildReservationDtoWithDates(now.plusMonths(2), now.plusMonths(2).plusDays(2));

			violations = validator.validate(reservationDto);

			assertThat(violations.size()).isEqualTo(1);
			violations.forEach(
					v -> assertThat(v.getConstraintDescriptor().getAnnotation().annotationType().getCanonicalName())
							.isEqualTo(AllowedStartDate.class.getCanonicalName()));
		}
	}

	@Nested
	public class StartDateBeforeEndDateValidator {
		@Test
		public void whenStartDateIsBeforeEndDate_thenNoValidationErrorThrown() {
			reservationDto = buildReservationDtoWithDates(now.plusDays(1), now.plusDays(2));

			violations = validator.validate(reservationDto);

			assertThat(violations.size()).isZero();
		}

		@Test
		public void whenStartDateIsAfterEndDate_thenThrowsStartDateBeforeEndDateException() {
			reservationDto = buildReservationDtoWithDates(now.plusDays(4), now.plusDays(1));

			violations = validator.validate(reservationDto);

			assertThat(violations.size()).isEqualTo(1);
			violations.forEach(
					v -> assertThat(v.getConstraintDescriptor().getAnnotation().annotationType().getCanonicalName())
							.isEqualTo(StartDateBeforeEndDate.class.getCanonicalName()));
		}

		@Test
		public void whenStartDateIsEqualsToEndDate_thenThrowsStartDateBeforeEndDateException() {
			reservationDto = buildReservationDtoWithDates(now.plusDays(1), now.plusDays(1));

			violations = validator.validate(reservationDto);

			assertThat(violations.size()).isEqualTo(1);
			violations.forEach(
					v -> assertThat(v.getConstraintDescriptor().getAnnotation().annotationType().getCanonicalName())
							.isEqualTo(StartDateBeforeEndDate.class.getCanonicalName()));
		}

	}

	@Nested
	public class MaximumStayValidator {
		@Test
		public void whenDateRangeIsLessThanThreeDays_thenNoValidationErrorThrown() {
			reservationDto = buildReservationDtoWithDates(now.plusDays(1), now.plusDays(3));

			violations = validator.validate(reservationDto);

			assertThat(violations.size()).isZero();
		}

		@Test
		public void whenDateRangeIsMoreThanThreeDays_thenNoValidationErrorThrown() {
			reservationDto = buildReservationDtoWithDates(now.plusDays(2), now.plusDays(7));

			violations = validator.validate(reservationDto);

			assertThat(violations.size()).isEqualTo(1);
			violations.forEach(
					v -> assertThat(v.getConstraintDescriptor().getAnnotation().annotationType().getCanonicalName())
							.isEqualTo(MaximumStay.class.getCanonicalName()));
		}
	}

	private ReservationDto buildReservationDtoWithDates(LocalDate startDate, LocalDate endDate) {
		return new ReservationDto(1L, 1L, 1L, "test@gmail.com", "Test User", startDate, endDate, true);
	}
}
