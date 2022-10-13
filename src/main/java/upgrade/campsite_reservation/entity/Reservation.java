package upgrade.campsite_reservation.entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import upgrade.campsite_reservation.dto.ReservationDto;

//@AllArgsConstructor
//@Data
@Entity
@Table(name = "reservation")
@Builder
@Generated
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Reservation extends DateAudit {
	private static final long serialVersionUID = 1L;
	
	public Reservation() {
		super();
	}

	public Reservation(Long version, 
					   Campsite campsite, 
					   String email, 
					   String fullName, 
					   LocalDate startDate,
					   LocalDate endDate, 
					   boolean active) {
		this.version = version;
		this.campsite = campsite;
		this.email = email;
		this.fullName = fullName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.active = active;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;
	
	@Version
	@Column(name = "version", nullable = false)
	private Long version;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "campsite_id")
	private Campsite campsite;
	
	@Column(name = "email", nullable = false, length = 100)
	private String email;

	@Column(name = "full_name", nullable = false, length = 100)
	private String fullName;
	
	@Column(name = "start_date", nullable = false)
	private LocalDate startDate;

	@Column(name = "end_date", nullable = false)
	private LocalDate endDate;

	@Column(name = "active", nullable = false)
	private boolean active;

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public LocalDate getStartDate() {
		return startDate;
	}
	
	public LocalDate getEndDate() {
		return endDate;
	}
	
	public Long getCampsiteId() {
		return campsite.getId();
	}
	
	public List<LocalDate> getDatesBetween() {
	    return startDate.datesUntil(endDate).toList();
	}
	
	public boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public ReservationDto toReservationDto() {		
		return new ReservationDto(
				this.id,
				this.version,
				this.campsite.getId(),
				this.email,
				this.fullName,
				this.startDate,
				this.endDate,
				this.active);
	}

}
