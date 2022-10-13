package upgrade.campsite_reservation.exceptions;

public class ReservationNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public ReservationNotFoundException(String message) {
		super(message);
	}
}
