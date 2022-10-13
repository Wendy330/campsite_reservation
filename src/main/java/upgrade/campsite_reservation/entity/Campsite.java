package upgrade.campsite_reservation.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;

@Entity
@Table(name = "campsite")
@Builder
@AllArgsConstructor
@Generated
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Campsite extends DateAudit {
	private static final long serialVersionUID = 1L;
	
	public Campsite() {
		super();
	}
	
	public Campsite(Long id, int capacity, boolean availble) {
		this.id = id;
		this.capacity = capacity;
		this.availble = availble;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;
	
	@Column(name = "capacity", nullable = false)
	private int capacity;

	@Column(name = "availble", nullable = false)
	private boolean availble;
	
	public Long getId() {
		return id;
	}
}
