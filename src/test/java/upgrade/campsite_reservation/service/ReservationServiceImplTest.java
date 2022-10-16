package upgrade.campsite_reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import upgrade.campsite_reservation.entity.Campsite;
import upgrade.campsite_reservation.entity.Reservation;
import upgrade.campsite_reservation.exceptions.ReservationDatesNotAvailableException;
import upgrade.campsite_reservation.exceptions.ReservationNotFoundException;
import upgrade.campsite_reservation.exceptions.UnavailableReservationException;
import upgrade.campsite_reservation.repository.ReservationRepository;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceImplTest {

	private static final Long CAMPSITE_ID = 1L;

	@Mock
	private ReservationRepository repo;

	@InjectMocks
	private ReservationServiceImpl service;

	private LocalDate now;
	private Reservation existingReservation;
	private Reservation newReservation;

	@BeforeEach
	void beforeEach() {
		now = LocalDate.now();
		existingReservation = null;
		newReservation = null;
	}
	
	@Nested
	public class FindAvailableDates {

		@Test
		public void whenStartDateIsBeforeToday_throwsIllegalArgumentException() {
			assertThrows(ReservationDatesNotAvailableException.class, () -> service.findAvailableDates(now.minusDays(1), now.plusDays(1), CAMPSITE_ID));
		}
		
		@Test
		public void whenStartDateIsToday_throwsIllegalArgumentException() {
			assertThrows(ReservationDatesNotAvailableException.class, () -> service.findAvailableDates(now, now.plusDays(1), CAMPSITE_ID));
		}
		
		@Test
		public void whenEndDateIsToday_throwsIllegalArgumentException() {
			assertThrows(ReservationDatesNotAvailableException.class, () -> service.findAvailableDates(now.minusDays(1), now, CAMPSITE_ID));
		}
		
		@Test
		public void whenEndDateIsBeforeToday_throwsIllegalArgumentException() {
			assertThrows(ReservationDatesNotAvailableException.class, () -> service.findAvailableDates(now.minusDays(2), now.minusDays(1), CAMPSITE_ID));
		}
		
		@Test
		public void whenStartDateIsAfterEndDate_throwsIllegalArgumentException() {
			assertThrows(ReservationDatesNotAvailableException.class, () -> service.findAvailableDates(now.plusDays(2), now.plusDays(1), CAMPSITE_ID));
		}
		
		@Test
		public void whenReservationDatesIsLongerThanRange_thenReturnsEndDate() {
			buildExistingReservationWithDates(now.plusDays(1), now.plusDays(4));
			
			when(repo.findByDateRange(any(), any(), any())).thenReturn(Collections.singletonList(existingReservation));
			
			List<LocalDate> returned = service.findAvailableDates(now.plusDays(2), now.plusDays(3), CAMPSITE_ID);
			
			assertThat(returned).isEqualTo(Collections.emptyList());
		}
		
		@Test
		public void whenNoReservationDatesWithinRange_thenReturnsAvailableDatesInAMonth() {
			LocalDate startDate = now.plusDays(1);
			LocalDate endDate = now.plusDays(3);
			
			when(repo.findByDateRange(any(), any(), any())).thenReturn(Collections.emptyList());
			
			List<LocalDate> expected = startDate.datesUntil(endDate.plusDays(1)).toList();
			List<LocalDate> returned = service.findAvailableDates(startDate, endDate, CAMPSITE_ID);
			
			assertThat(returned).hasSameSizeAs(expected).hasSameElementsAs(expected);
		}
		
		@Test
		public void whenReservationDatesIsTheSameRange_thenReturnsEndDate() {
			LocalDate startDate = now.plusDays(1);
			LocalDate endDate = now.plusDays(3);
			buildExistingReservationWithDates(startDate, endDate);
			
			when(repo.findByDateRange(any(), any(), any())).thenReturn(Collections.singletonList(existingReservation));
			
			List<LocalDate> expected = Collections.singletonList(endDate);
			List<LocalDate> returned = service.findAvailableDates(startDate, endDate, CAMPSITE_ID);
			
			assertThat(returned).isEqualTo(expected);
		}
	}

	@Nested
	public class FindById {
		
		@Test
		public void whenIdDoesNotExist_throwsNotFoundException() {
			when(repo.findById(any())).thenReturn(Optional.empty());
			assertThrows(ReservationNotFoundException.class, () -> service.findById(any()));
		}
		
		@Test
		public void whenIdExists_thenReturnsExistingReservationRecord() {
			buildExistingReservationWithDates(now.plusDays(1), now.plusDays(3));
			
			when(repo.findById(any())).thenReturn(Optional.of(existingReservation));
			
			Reservation result = service.findById(1L);
			
			assertThat(result).isEqualTo(existingReservation);
			verify(repo).findById(1L);
		}
		
	}
	
	@Nested
	public class CreateReservation {
		@Test
		public void whenReservationDatesAreNotAvailable_throwsReservationDatesNotAvailableException() {
			buildExistingReservationWithDates(now.plusDays(1), now.plusDays(3));
			
			when(repo.findByDateRange(any(), any(), any())).thenReturn(Collections.singletonList(existingReservation));
			
			buildNewReservationWithDates(now.plusDays(2), now.plusDays(3));
			
			assertThrows(ReservationDatesNotAvailableException.class, () -> service.createReservation(newReservation));
		}
		
		@Test
		public void whenReservationDatesAreAvailable_thenReservationIsCreatedSuccessfully() {			
			when(repo.findByDateRange(any(), any(), any())).thenReturn(Collections.emptyList());
			
			buildNewReservationWithDates(now.plusDays(2), now.plusDays(3));
			
			service.createReservation(newReservation);
			
			assertTrue(newReservation.getActive());
			verify(repo).findByDateRange(newReservation.getStartDate(), newReservation.getEndDate(), newReservation.getCampsiteId());
			verify(repo).save(newReservation);
		}
		
//		@Test
//		public void whenTwoReservationsHaveSameDateRange_thenOnlyOneReservationIsCreatedSuccessfully() {			
//			Reservation reservation1 = new Reservation(1L, new Campsite(CAMPSITE_ID, 30, true), "new_user@gmail.com", "New User 1", now.plusDays(1), now.plusDays(2), true);
//			Reservation reservation2 = new Reservation(1L, new Campsite(CAMPSITE_ID, 30, true), "new_user@gmail.com", "New User 2", now.plusDays(1), now.plusDays(2), true);
//			List<Reservation> newReservations = List.of(reservation1, reservation2);
//
//			ExecutorService executor = Executors.newFixedThreadPool(newReservations.size());
//			newReservations.forEach(b -> executor.execute(() -> service.createReservation(b)));
//		    executor.shutdown();
//		    
//		    
//		    Iterable<Reservation> results = repo.findAll();
//		    assertThat(results).hasSize(1);
//		}
	}
	
	@Nested
	public class UpdateReservation {
		@Test
		public void whenExistingReservationHasBeenCancelled_throwsReservationDatesNotAvailableException() {
			buildExistingReservationWithDates(now.plusDays(1), now.plusDays(3));
			existingReservation.setActive(false);
			
			when(repo.findById(any())).thenReturn(Optional.of(existingReservation));
			
			assertThrows(UnavailableReservationException.class, () -> service.updateReservation(existingReservation));
		}
		
		@Test
		public void whenReservationDatesAreAvailable_thenReservationIsUpdatedSuccessfully() {
			buildExistingReservationWithDates(now.plusDays(1), now.plusDays(3));
			
			when(repo.findById(any())).thenReturn(Optional.of(existingReservation));
			when(repo.findByDateRange(any(), any(), any())).thenReturn(Collections.singletonList(existingReservation));
			
			buildExistingReservationWithDates(now.plusDays(4), now.plusDays(5));
			
			service.updateReservation(existingReservation);
			
			assertTrue(existingReservation.getActive());
			verify(repo).save(existingReservation);
			verify(repo).findById(1L);
			verify(repo).findByDateRange(existingReservation.getStartDate(), existingReservation.getEndDate(), existingReservation.getCampsiteId());
		}
	}
	
	@Nested
	public class CancelReservation {
		@Test
		public void whenExistingReservationIsFound_thenCancelReservationSuccessfully() {
			buildExistingReservationWithDates(now.plusDays(1), now.plusDays(3));
			
			when(repo.findById(any())).thenReturn(Optional.of(existingReservation));
			
			boolean active = service.cancelReservation(existingReservation.getId());
			
			assertFalse(active);
			verify(repo).findById(1L);
			verify(repo).save(existingReservation);
		}
	}
	
	
	private void buildExistingReservationWithDates(LocalDate startDate, LocalDate endDate) {
		existingReservation = new Reservation(1L, new Campsite(CAMPSITE_ID, 30, true), "test@gmail.com", "Test User", startDate, endDate, true);
		existingReservation.setId(1L);
	}
	
	private void buildNewReservationWithDates(LocalDate startDate, LocalDate endDate) {
		newReservation = new Reservation(2L, new Campsite(CAMPSITE_ID, 30, true), "new_user@gmail.com", "New User", startDate, endDate, true);
		newReservation.setId(2L);
	}
	
}
