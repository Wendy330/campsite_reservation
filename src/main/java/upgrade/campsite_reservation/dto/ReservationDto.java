package upgrade.campsite_reservation.dto;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Version;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.modelmapper.ModelMapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Generated;
import lombok.experimental.Accessors;
import upgrade.campsite_reservation.entity.Campsite;
import upgrade.campsite_reservation.entity.Reservation;
import upgrade.campsite_reservation.validators.AllowedStartDate;
import upgrade.campsite_reservation.validators.MaximumStay;
import upgrade.campsite_reservation.validators.StartDateBeforeEndDate;

//@Data
//@AllArgsConstructor
@Builder
@Generated
@Accessors(fluent = true)
@AllowedStartDate
@MaximumStay
@StartDateBeforeEndDate
public class ReservationDto {
	
	public ReservationDto(Long id, 
						  Long version, 
						  Long campsiteId,  
						  String email,
						  String fullName, 
						  LocalDate startDate, 
						  LocalDate endDate,
						  boolean active) {
		this.id = id;
		this.version = version;
		this.campsiteId = campsiteId;
		this.email = email;
		this.fullName = fullName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.active = active;
	}

	private Long id;
	
	private Long version;

	private Long campsiteId;
	
	@NotEmpty
    @Size(max = 100)
	private String email;

	@NotEmpty
	@Size(max = 100)
	private String fullName;
	
	@NotNull
	private LocalDate startDate;
	
	@NotNull
	private LocalDate endDate;
	
	@NotNull
	private boolean active;
	
	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public Long getCampsiteId() {
		return campsiteId;
	}

	public void setCampsiteId(Long campsiteId) {
		this.campsiteId = campsiteId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Reservation toReservation() {
		return new Reservation(
				this.version,
				new Campsite(this.campsiteId, 30, true),
				this.fullName,
				this.email,
				this.startDate,
				this.endDate,
				this.active
				);
	}

}
