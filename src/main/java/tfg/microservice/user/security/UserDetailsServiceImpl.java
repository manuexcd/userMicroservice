package tfg.microservice.user.security;

import static java.util.Collections.emptyList;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import tfg.microservice.user.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	private UserRepository users;

	public UserDetailsServiceImpl(UserRepository applicationUserRepository) {
		this.users = applicationUserRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) {
		tfg.microservice.user.model.User applicationUser = users.findByEmail(username);
		if (applicationUser == null) {
			throw new UsernameNotFoundException(username);
		}
		return new User(applicationUser.getEmail(), applicationUser.getPassword(), emptyList());
	}
}