package horizon.time.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserDto {
	@NotBlank(message = "{general.notempty}")
	@Email(message = "{general.invalidemail}")
	@Size(min = 6, max = 30, message = "{general.size}")
	private String username;

	@org.springframework.data.annotation.Transient
	@NotBlank(message = "{general.notempty}")
	@Size(min = 6, max = 20, message = "{general.size}")
	private String password;

	public UserDto() {
		super();
	}

	public UserDto(String username, String password) {
		super();
		this.username = username;
		setPassword(password);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}