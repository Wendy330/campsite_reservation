package upgrade.campsite_reservation.rest;

import static java.util.Objects.isNull;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import upgrade.campsite_reservation.dto.ReservationDto;
import upgrade.campsite_reservation.entity.Reservation;
import upgrade.campsite_reservation.service.ReservationService;

@RestController
@AllArgsConstructor
public class ReservationRestController {

	@Autowired
	private ReservationService reservationService;
	
	@GetMapping(value="/available-dates", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<LocalDate>> getAvailableDates(LocalDate startDate, LocalDate endDate, Long campsiteId) {
		if (isNull(startDate)) {
	      startDate = LocalDate.now().plusDays(1);
	    }
		
	    if (isNull(endDate)) {
	      endDate = startDate.plusMonths(1);
	    }
	    
	    List<LocalDate> availableDates = reservationService.findAvailableDates(startDate, endDate, campsiteId);
	    
	    return new ResponseEntity<>(availableDates, HttpStatus.OK);
	}
	
	@GetMapping(value = "reservations/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ReservationDto> getReservation(@PathVariable() Long id) {
		ReservationDto dto = reservationService.findById(id).toReservationDto();
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	
	@PostMapping(value = "reservation", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Reservation> addReservation(@RequestBody @Valid ReservationDto dto) {		
		Reservation result = reservationService.createReservation(dto.toReservation());
		return new ResponseEntity<>(result, HttpStatus.CREATED);
	}
	
	@PutMapping(value = "reservations/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ReservationDto> updateReservation(@PathVariable("id") Long id, @RequestBody @Valid ReservationDto dto) {
		Reservation updatedReservation = dto.toReservation();
		updatedReservation.setId(id);
		ReservationDto result = reservationService.updateReservation(updatedReservation).toReservationDto();
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "reservations/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> cancelReservation(@PathVariable("id") Long id) {
		boolean active = reservationService.cancelReservation(id);
	    if (!active) {
	      return new ResponseEntity<>(HttpStatus.OK);
	    }
	    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

}
