package upgrade.campsite_reservation.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import upgrade.campsite_reservation.entity.Reservation;

@Service
public interface ReservationService {
	
	List<LocalDate> findAvailableDates(LocalDate startDate, LocalDate endDate, Long campsiteId);

	Reservation findById(Long id);
	
	Reservation createReservation(Reservation reservation);

	Reservation updateReservation(Reservation reservation);

	boolean cancelReservation(Long id);

}
