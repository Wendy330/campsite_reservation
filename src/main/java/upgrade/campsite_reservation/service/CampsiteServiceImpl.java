package upgrade.campsite_reservation.service;

import upgrade.campsite_reservation.entity.Campsite;
import upgrade.campsite_reservation.exceptions.CampsiteNotFoundException;
import upgrade.campsite_reservation.repository.CampsiteRepository;

public class CampsiteServiceImpl implements CampsiteService{

	private CampsiteRepository repository;
	
	@Override
	public Campsite findById(Long id) {
		return repository.findById(id).orElseThrow(() -> new CampsiteNotFoundException("No campsite was found with id %d" + id));
	}

}
