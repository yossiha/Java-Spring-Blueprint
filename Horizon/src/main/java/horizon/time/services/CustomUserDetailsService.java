package horizon.time.services;

import horizon.time.persistence.repo.UserRepository;
import horizon.time.persistence.model.Role;
import horizon.time.persistence.model.User;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private HttpServletRequest request;

	public CustomUserDetailsService() {
		super();
	}

	private Collection<? extends GrantedAuthority> getAuthorities(Set<Role> roles) {
		return getGrantedAuthorities(roles);
	}

	private Set<GrantedAuthority> getGrantedAuthorities(Set<Role> roles) {
		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

		roles.stream().forEach(role -> // * faster than lambda
		grantedAuthorities.add(new SimpleGrantedAuthority(role.getName())));

		return grantedAuthorities;
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(final String username) {
		User user = userRepository.findByUsername(username);
		if (user == null)
			throw new UsernameNotFoundException(username);

		request.getSession().setAttribute("loggedUserId", user.getId());

		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), true,
				true, true, true, getAuthorities(user.getRoles()));
	}
}
