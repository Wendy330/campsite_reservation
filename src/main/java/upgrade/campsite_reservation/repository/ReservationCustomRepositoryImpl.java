package upgrade.campsite_reservation.repository;

import static javax.persistence.LockModeType.PESSIMISTIC_WRITE;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;
import upgrade.campsite_reservation.entity.Reservation;

@Slf4j
public class ReservationCustomRepositoryImpl implements ReservationCustomRepository {
	
	private EntityManager entityManager;
	
	@Autowired
	public ReservationCustomRepositoryImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Reservation> findByDateRange(LocalDate startDate, LocalDate endDate, Long campsiteId) {
	    return entityManager.createQuery(FIND_WITH_DATE_RANGE)
	    		.setParameter(1, startDate)
	    		.setParameter(2, endDate)
	    		.setParameter(3, campsiteId)
		        .setLockMode(PESSIMISTIC_WRITE)
		        .setHint("javax.persistence.query.timeout", 3000)
		        .getResultList();
	}
}
