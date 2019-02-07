package horizon.time.persistence.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.google.gson.annotations.Expose;

import horizon.time.persistence.model.enums.UserStatus;

@Entity
@Table(name = "users")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	// Using a string is OK for small applications
	@Column(name = "status", columnDefinition = "varchar(12) default 'PENDING'")
	@Enumerated(value = EnumType.STRING)
	private UserStatus status;

	@NotBlank
	@Size(max = 25)
	@NaturalId
	@Column(name = "username", nullable = false, unique = true)
	private String username;

	@org.springframework.data.annotation.Transient // spring transient
	@NotBlank
	@Size(min = 60, max = 60)
	@Column(name = "password", nullable = false)
	@Expose(serialize = false, deserialize = true) // gson ignore for json
	private String password;
	public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	@Column(name = "created_at", insertable = true, updatable = false)
	private Date createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	@Column(name = "updated_at", insertable = false, updatable = true)
	private Date updatedAt;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "businesses_users", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
			@JoinColumn(name = "business_id") })
	private Set<Business> businesses = new HashSet<>();

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	private Set<Role> roles = new HashSet<>();

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true) // , mappedBy = "user")
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private Set<Media> media = new HashSet<>();

	public User() {

	}

	public User(String username, String password) {
		this.username = username;
		setPassword(password);
	}

	public User(String username, String password, UserStatus status) {
		this.status = status;
		this.username = username;
		setPassword(password);
	}

	public User(String username, String password, Set<Role> roles) {
		this.username = username;
		setPassword(password);
		this.roles = roles;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = PASSWORD_ENCODER.encode(password);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public UserStatus getStatusType() {
		return status;
	}

	public String getStatus() {
		return status.toString();
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreateAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Set<Business> getBusinesses() {
		return businesses;
	}

	public void setBusinesses(Set<Business> businesses) {
		this.businesses = businesses;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Set<Media> getMedia() {
		return media;
	}

	public void setMedia(Set<Media> media) {
		this.media = media;
	}

	public boolean isNew() {
		return this.id == null;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return this.getId().equals(other.id);
	}

	@Override
	public String toString() {
		return "Users [id=" + id + ", username=" + username + ", status=" + status +
		// ", email=" + email + ", phone=" + phone + ", address=" + address +
				", createDate=" + createdAt + "]";
	}

}