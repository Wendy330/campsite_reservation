package upgrade.campsite_reservation.service;

import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import upgrade.campsite_reservation.entity.Reservation;
import upgrade.campsite_reservation.exceptions.ReservationDatesNotAvailableException;
import upgrade.campsite_reservation.exceptions.ReservationNotFoundException;
import upgrade.campsite_reservation.exceptions.UnavailableReservationException;
import upgrade.campsite_reservation.repository.ReservationRepository;

@Service
public class ReservationServiceImpl implements ReservationService {
	
	@Autowired
	private ReservationRepository repository;

	@Override
	@Transactional
	public List<LocalDate> findAvailableDates(LocalDate startDate, LocalDate endDate, Long campsiteId) {
		LocalDate now = LocalDate.now();
		
		if(startDate.isBefore(now) || startDate.equals(now)) {
			throw new ReservationDatesNotAvailableException("Start Date must be later than today"); 
		}
		if(endDate.isBefore(now) || endDate.equals(now)) {
			throw new ReservationDatesNotAvailableException("End Date must be later than today"); 
		}
		if(startDate.isAfter(endDate)) {
			throw new ReservationDatesNotAvailableException("End Date must be later than Start Date"); 
		}
		
		List<LocalDate> dates = startDate.datesUntil(endDate.plusDays(1)).collect(toList());
		List<Reservation> reservations = repository.findByDateRange(startDate, endDate, campsiteId);
		reservations.forEach(r -> dates.removeAll(r.getDatesBetween()));
	    return dates;
	}
	
	@Override
	@Transactional
	public Reservation findById(Long id) {
		return repository.findById(id).orElseThrow(() -> new ReservationNotFoundException("Cannot find Reservation %d" + id));
	}

	@Override
	@Transactional
	public Reservation createReservation(Reservation reservation) {
		validateReservationDates(reservation);
		
		reservation.setActive(true);
		
		return repository.save(reservation);
	}

	@Override
	@Transactional
	public Reservation updateReservation(Reservation reservation) {
		Reservation existing = findById(reservation.getId());
		
		if(!existing.getActive()) {
			throw new UnavailableReservationException("Reservation has been cancelled");
		}
		
		validateReservationDates(reservation);
		
		reservation.setActive(existing.getActive());
		
		return repository.save(reservation);
	}

	@Override
	@Transactional
	public boolean cancelReservation(Long id) {
		Reservation existing = findById(id);
		existing.setActive(false);
		repository.save(existing);
		
		return existing.getActive();
	}
	
	private void validateReservationDates(Reservation reservation) {
		List<LocalDate> availableDates = findAvailableDates(reservation.getStartDate(), reservation.getEndDate(), reservation.getCampsiteId());
		
		if(!availableDates.containsAll(reservation.getDatesBetween())) {
			throw new ReservationDatesNotAvailableException(String.format("No available dates from %s to %s", reservation.getStartDate(), reservation.getEndDate()));
		}
	}

}
