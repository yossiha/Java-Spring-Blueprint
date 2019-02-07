package horizon.time.persistence.model;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.concurrent.ThreadLocalRandom;

@Entity
@Table(name = "businesses")
public class Business {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@NotBlank
	@Size(min = 3, max = 20)
	@Column(name = "name", nullable = false)
	private String name;

	// TODO
	// @Min(value = 0L)
	// @Column(name = "location_id", nullable = true)
	// private Long locationId;

	@NotBlank
	@Size(max = 300)
	@Column(name = "description_short", columnDefinition = "TEXT", nullable = false)
	private String descriptionShort;

	@NaturalId
	@Size(min = 1100000, max = 952234462)
	@Column(name = "slug", nullable = false)
	private int slug;

	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	@Column(name = "created_at", insertable = true, updatable = false)
	private Date createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	@Column(name = "updated_at", insertable = false, updatable = true)
	private Date updatedAt;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "businesses_users", joinColumns = { @JoinColumn(name = "business_id") }, inverseJoinColumns = {
			@JoinColumn(name = "user_id") })
	private Set<User> users = new HashSet<>();

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true) // , mappedBy = "user")
	@JoinColumn(name = "object_id", referencedColumnName = "id")
	private Set<Media> media = new HashSet<>();

	public Business() {
		super();
		this.randomSlug();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescriptionShort() {
		return descriptionShort;
	}

	public void setDescriptionShort(String descriptionShort) {
		this.descriptionShort = descriptionShort;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// TODO
	/*
	 * public Long getLocation() { return locationId; }
	 * 
	 * public void setLocation(Long locationId) { this.locationId = locationId; }
	 */

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreateAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUser(User user) {
		this.users = Collections.singleton(user);
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public boolean isNew() {
		return this.id == null;
	}

	public Set<Media> getMedia() {
		return media;
	}

	public void setMedia(Set<Media> media) {
		this.media = media;
	}

	public int getSlug() {
		return slug;
	}

	public void setSlug(int slug) {
		this.slug = slug;
	}

	public void randomSlug() {
		slug = ThreadLocalRandom.current().nextInt(1100000, 952234462 + 1);
	}
}
