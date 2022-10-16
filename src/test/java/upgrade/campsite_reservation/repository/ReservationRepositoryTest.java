package upgrade.campsite_reservation.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import upgrade.campsite_reservation.CampsiteReservationApplication;
import upgrade.campsite_reservation.entity.Campsite;
import upgrade.campsite_reservation.entity.Reservation;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest(classes = CampsiteReservationApplication.class)
public class ReservationRepositoryTest {
	
	private static final Long CAMPSITE_ID = 1L;
	
	@Autowired
	private ReservationRepository reservationRepository;

	private LocalDate now;
	private Reservation existingReservation;
	
	@BeforeEach
	void beforeEach() {
		reservationRepository.deleteAll();
		now = LocalDate.now();
		existingReservation = null;
	}
	
	@Nested
	public class FindByDateRange {
		
		@Test
		public void whenReservationDatesBeforeRange_thenNoRecordIsFound() {
			buildReservationWithDates(now.plusDays(1), now.plusDays(3));
			
			List<Reservation> results = reservationRepository.findByDateRange(now.plusDays(5), now.plusDays(7), CAMPSITE_ID);
			
			assertThat(results).isEmpty();
		}
		
		@Test
		public void whenReservationDatesAfterRange_thenNoRecordIsFound() {
			buildReservationWithDates(now.plusDays(5), now.plusDays(7));
			
			List<Reservation> results = reservationRepository.findByDateRange(now.plusDays(1), now.plusDays(3), CAMPSITE_ID);
			
			assertThat(results).isEmpty();
		}
		
		@Test
		public void whenReservationStartDateIsWithinRangeButEndDateIsAfterRange_thenReturnsRecord() {
			buildReservationWithDates(now.plusDays(6), now.plusDays(8));
			
			List<Reservation> results = reservationRepository.findByDateRange(now.plusDays(2), now.plusDays(7), CAMPSITE_ID);
			
			assertThat(results.size()).isEqualTo(1);
			assertThat(results.get(0)).isEqualTo(existingReservation);
		}
		
		@Test
		public void whenReservationEndDateIsWithinRange_thenReturnsRecord() {
			buildReservationWithDates(now.plusDays(1), now.plusDays(3));
			
			List<Reservation> results = reservationRepository.findByDateRange(now.plusDays(2), now.plusDays(7), CAMPSITE_ID);
			
			assertThat(results.size()).isEqualTo(1);
			assertThat(results.get(0)).isEqualTo(existingReservation);
		}
		
		@Test
		public void whenReservationDatesAreBothWithinRange_thenReturnsRecord() {
			buildReservationWithDates(now.plusDays(3), now.plusDays(5));
			
			List<Reservation> results = reservationRepository.findByDateRange(now.plusDays(2), now.plusDays(7), CAMPSITE_ID);
			
			assertThat(results.size()).isEqualTo(1);
			assertThat(results.get(0)).isEqualTo(existingReservation);
		}
		
		@Test
		public void whenReservationDatesAreLongerThanRange_thenReturnsRecord() {
			buildReservationWithDates(now.plusDays(2), now.plusDays(5));
			
			List<Reservation> results = reservationRepository.findByDateRange(now.plusDays(3), now.plusDays(4), CAMPSITE_ID);
			
			assertThat(results.size()).isEqualTo(1);
			assertThat(results.get(0)).isEqualTo(existingReservation);
		}
	}
	
	
	private void buildReservationWithDates(LocalDate startDate, LocalDate endDate) {
		Reservation dto = new Reservation(1L, new Campsite(CAMPSITE_ID, 30, true), "test@gmail.com", "Test User", startDate, endDate, true);
		existingReservation = reservationRepository.save(dto);
	}

}
