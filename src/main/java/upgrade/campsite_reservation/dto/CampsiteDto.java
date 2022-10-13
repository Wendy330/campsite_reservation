package upgrade.campsite_reservation.dto;

import javax.validation.constraints.NotNull;

public class CampsiteDto {
	
	@NotNull
	private Long id;
	
	@NotNull
	private int capacity;
	
	@NotNull
	private boolean availble;

}
