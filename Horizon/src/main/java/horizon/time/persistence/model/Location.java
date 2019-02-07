package horizon.time.persistence.model;

import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "locations")

public class Location {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@NotBlank
	@Size(min = 3, max = 20)
	@Column(name = "name", nullable = false)
	private String name;

	@Min(value = 0L)
	@Column(name = "location_id", nullable = true)
	private Long locationId;

	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	@Column(name = "created_at", insertable = true, updatable = false)
	private Date createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	@Column(name = "updated_at", insertable = false, updatable = true)
	private Date updatedAt;

	public Location() {
		super();
	}

	public Location(String name, Long locationId) {// , Set<User> users) {
		super();
		this.name = name;
		this.locationId = locationId;
		// this.users = users;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getLocation() {
		return locationId;
	}

	public void setLocation(Long locationId) {
		this.locationId = locationId;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreateAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public boolean isNew() {
		return null == getId();
	}
}
