package upgrade.campsite_reservation.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import upgrade.campsite_reservation.entity.Reservation;

public interface ReservationCustomRepository {
	String FIND_WITH_DATE_RANGE = 
			"select r from Reservation r "
		      + "where ((:startDate <= r.startDate and r.startDate <= :endDate) "
		      + "or (:startDate < r.endDate and r.endDate <= :endDate) "
		      + "or (r.startDate < :startDate and :endDate < r.endDate)) "
		      + "and r.active = true "
		      + "and r.campsite.id = :campsiteId";

	@Query(FIND_WITH_DATE_RANGE)
	List<Reservation> findByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("campsiteId")Long campsiteId);

}
