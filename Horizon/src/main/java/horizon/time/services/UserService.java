package horizon.time.services;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import horizon.time.WebContextHolder;
import horizon.time.dto.UserDto;
import horizon.time.persistence.model.User;
import horizon.time.persistence.repo.RoleRepository;
import horizon.time.persistence.repo.UserRepository;

@Service
@Transactional

public class UserService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private WebContextHolder webContextHolder;

	public User registerNewUserAccount(final UserDto accountDto) throws Exception {
		final User user = new User();

		if (userRepository.existsByUsername(accountDto.getUsername())) {
			throw new Exception("There is an account with that email adress: " + accountDto.getUsername());
		} else {
			user.setUsername(accountDto.getUsername());
			user.setPassword(accountDto.getPassword());
			user.setRoles(new HashSet<>(Arrays.asList(roleRepository.findByName("ROLE_USER"))));

			return userRepository.save(user);
		}
	}

	public void authWithoutPassword(User user) {
		List<GrantedAuthority> authorities = user.getRoles().stream().map(p -> new SimpleGrantedAuthority(p.getName()))
				.collect(Collectors.toList());

		Authentication authentication = new UsernamePasswordAuthenticationToken(
				new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), true,
						true, true, true, authorities),
				null, authorities);

		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	public String getLoggedUsername() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

	public User getLoggedUser() {
		return userRepository.findByUsername(getLoggedUsername());
	}

	public long getLoggedUserId() {
		return (long) webContextHolder.getSession().getAttribute("loggedUserId");
	}
}
