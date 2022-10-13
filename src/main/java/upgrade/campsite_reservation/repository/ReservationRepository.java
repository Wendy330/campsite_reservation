package upgrade.campsite_reservation.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import upgrade.campsite_reservation.entity.Reservation;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Long>, ReservationCustomRepository {

	@Query(FIND_WITH_DATE_RANGE)
	List<Reservation> findByDateRange(LocalDate startDate, LocalDate endDate, Long campsiteId);
}
