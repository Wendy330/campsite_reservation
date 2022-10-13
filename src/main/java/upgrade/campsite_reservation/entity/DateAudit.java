package upgrade.campsite_reservation.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;
import lombok.Generated;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
@Generated
public abstract class DateAudit implements Serializable {
	private static final long serialVersionUID = 1L;

	@CreatedDate
	@Column(name = "created_time", updatable = false)
	private LocalDateTime createdTimestamp;
	
	@LastModifiedDate
	@Column(name = "last_modified_time")
	private LocalDateTime lastModifiedTimestamp;
}
