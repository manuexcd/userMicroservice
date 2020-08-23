package tfg.microservice.user.security;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import tfg.microservice.user.exception.EmailExistsException;
import tfg.microservice.user.model.User;
import tfg.microservice.user.repository.UserRepository;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailsServiceImplTest {

	@Mock
	private UserRepository dao;

	@InjectMocks
	private UserDetailsServiceImpl service;

	@Test
	public void testLoadUserByUsername() throws EmailExistsException {
		User user = new User("nombre", "apellidos", "direccion", "telefono", "email", "contraseña", "url");
		given(dao.findByEmail(anyString())).willReturn(user);
		assertNotNull(service.loadUserByUsername(user.getEmail()));
	}

	@Test(expected = UsernameNotFoundException.class)
	public void testLoadUserByUsernameNull() throws EmailExistsException {
		User user = new User("nombre", "apellidos", "direccion", "telefono", "email", "contraseña", "url");
		given(dao.findByEmail(anyString())).willReturn(null);
		assertNotNull(service.loadUserByUsername(user.getEmail()));
	}
}
