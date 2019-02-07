package horizon.time.persistence.model;

import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

//TODO stud

@Entity
@Table(name = "business_profiles")
public class BusinessProfile {
	@Id
	@Column(name = "id")
	private Long id;

	@OneToOne
	@MapsId
	private Business business;

	@NotBlank
	@Size(min = 3, max = 20)
	@Column(name = "owner_name")
	private String ownerName;

	@NotBlank
	@Size(min = 3, max = 20)
	@Column(name = "website")
	private String website;

	@NotBlank
	@Size(min = 3, max = 20)
	@Column(name = "description_long", columnDefinition = "TEXT", nullable = true)
	private String descriptionLong;

	// TODO set this properly
	/*
	 * @NotBlank
	 * 
	 * @Size(min=3, max = 20)
	 * 
	 * @Column(name = "city", nullable = false) private String categoryLocationArea;
	 * 
	 * @NotBlank
	 * 
	 * @Size(min=3, max = 20)
	 * 
	 * @Column(name = "city", nullable = false) private String categoryLocationCity;
	 */

	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	@Column(name = "created_at", insertable = true, updatable = false)
	private Date createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	@Column(name = "updated_at", insertable = false, updatable = true)
	private Date updatedAt;

	/*
	 * @ManyToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	 * 
	 * @JoinTable(name = "businesses_users", joinColumns = { @JoinColumn(name =
	 * "business_id") }, inverseJoinColumns = { @JoinColumn(name = "user_id") })
	 * private Set<User> users = new HashSet<>();
	 * 
	 * @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval =
	 * true)//, mappedBy = "user")
	 * 
	 * @JoinColumn(name = "object_id", referencedColumnName = "id") private
	 * Set<Media> media = new HashSet<>();
	 */

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreateAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public boolean isNew() {
		return this.business == null;
	}
}
