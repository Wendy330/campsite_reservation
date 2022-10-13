package upgrade.campsite_reservation.exceptions;

public class ReservationDatesNotAvailableException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ReservationDatesNotAvailableException(String message) {
	    super(message);
	  }
}
