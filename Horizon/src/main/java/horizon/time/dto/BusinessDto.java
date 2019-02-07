package horizon.time.dto;

import javax.validation.constraints.Size;

public class BusinessDto {
	private Long id = null;

	@Size(min = 3, max = 20, message = "{general.size}")
	private String name;

	@Size(min = 3, max = 4000, message = "{general.size}")
	private String descriptionShort;

	public BusinessDto() {
		super();
	}

	public BusinessDto(String name, String descriptionShort) {
		super();
		this.name = name;
		this.descriptionShort = descriptionShort;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}