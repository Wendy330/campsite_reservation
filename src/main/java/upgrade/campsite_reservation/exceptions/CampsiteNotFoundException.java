package upgrade.campsite_reservation.exceptions;

public class CampsiteNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public CampsiteNotFoundException(String message) {
		super(message);
	}

}
