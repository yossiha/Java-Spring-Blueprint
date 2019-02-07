package horizon.time.persistence.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "media")
public class Media implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Size(max = 25)
	@Column(name = "name", nullable = true, unique = false)
	private String name;

	@Size(max = 1500)
	@Column(name = "description", columnDefinition = "TEXT", nullable = true)
	private String description;

	@NotBlank
	@Size(max = 15)
	@Column(name = "mimetype", nullable = false, unique = false)
	private String mimetype;

	@NotBlank
	@Size(max = 30)
	@Column(name = "filename", nullable = false, unique = false)
	private String filename;

	@NotBlank
	@Size(max = 6)
	@Column(name = "ext", nullable = false, unique = false)
	private String ext;

	@Size(max = 20)
	@Column(name = "directory_prefix", nullable = true, unique = false)
	private String directoryPrefix;

	@Size(max = 30)
	@Column(name = "directory", nullable = true, unique = false)
	private String directory;

	@Size(max = 12)
	@Column(name = "object_type", nullable = true, unique = false)
	private String objectType;

	@Size(max = 12)
	@Column(name = "object_type_part", nullable = true, unique = false)
	private String objectTypePart;

	@Column(name = "object_id", nullable = true, unique = false)
	private Long objectId;

	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	@Column(name = "created_at", insertable = true, updatable = false)
	private Date createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	@Column(name = "updated_at", insertable = false, updatable = true)
	private Date updatedAt;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	@Where(clause = "object_type = business")
	@JoinColumn(name = "object_id", referencedColumnName = "id", updatable = false, insertable = false)
	private Business business;

	public Media() {

	}

	public Media(String objectType, String objectTypePart, long objectId, User user, String mimetype, String filename,
			String ext, String directory, String directoryPrefix) {
		this.objectType = objectType;
		this.objectTypePart = objectTypePart;
		this.objectId = objectId;
		this.user = user;
		this.mimetype = mimetype;
		this.filename = filename;
		this.ext = ext;
		this.directory = directory;
		this.directoryPrefix = directoryPrefix;

	}

	public boolean isNew() {
		return this.id == null;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMimetype() {
		return mimetype;
	}

	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getDirectoryPrefix() {
		return directoryPrefix;
	}

	public void setDirectoryPrefix(String directoryPrefix) {
		this.directoryPrefix = directoryPrefix;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreateAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getObjectTypePart() {
		return objectTypePart;
	}

	public void setObjectTypePart(String objectTypePart) {
		this.objectTypePart = objectTypePart;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		this.business = business;
	}

}