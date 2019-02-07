package horizon.time.persistence.model;

import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "addresses")

public class Address {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Size(min = 2, max = 20)
	@Column(name = "address_1", nullable = false)
	private String address_1;

	@Column(name = "address_2", nullable = true)
	private Long address_2;

	@Size(min = 2, max = 16)
	@Column(name = "city", nullable = false)
	private String city;

	@Size(min = 2, max = 2, message = "{generic.invalidstate}")
	@Column(name = "state", nullable = false)
	private String state;

	@Size(min = 4, max = 12)
	@Column(name = "zip", nullable = false)
	private String zip;

	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	@Column(name = "created_at", insertable = true, updatable = false)
	private Date createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	@Column(name = "updated_at", insertable = false, updatable = true)
	private Date updatedAt;

	public Address() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreateAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getAddress_1() {
		return address_1;
	}

	public void setAddress_1(String address_1) {
		this.address_1 = address_1;
	}

	public Long getAddress_2() {
		return address_2;
	}

	public void setAddress_2(Long address_2) {
		this.address_2 = address_2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}
}
