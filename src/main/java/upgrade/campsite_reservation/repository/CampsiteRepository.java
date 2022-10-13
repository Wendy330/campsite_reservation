package upgrade.campsite_reservation.repository;

import org.springframework.data.repository.CrudRepository;

import upgrade.campsite_reservation.entity.Campsite;

public interface CampsiteRepository extends CrudRepository<Campsite, Long> {

}
