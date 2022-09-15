package tfg.microservice.user.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.simplify4u.sjf4jmock.LoggerMock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import tfg.microservice.user.exception.EmailExistsException;
import tfg.microservice.user.exception.UserNotFoundException;
import tfg.microservice.user.mail.MailSender;
import tfg.microservice.user.model.User;
import tfg.microservice.user.repository.UserRepository;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

	@Mock
	private UserRepository dao;

	@Mock
	private BCryptPasswordEncoder encoder;

	@Mock
	private MailSender mailSender;

	@InjectMocks
	private UserServiceImpl service;

	private Pageable pageRequest;

	@Before
	public void setup() {
		LoggerMock.clearInvocations();
	}

	@Test
	public void testRegisterNewUserAccount() throws EmailExistsException {
		User user = new User();
		user.setName("Nombre");
		user.setSurname("Apellidos");
		user.setEmail("aaa");
		given(dao.findByEmail(anyString())).willReturn(user);
		given(dao.existsById(anyLong())).willReturn(false);
		given(dao.save(any())).willReturn(user);
		given(encoder.encode(any())).willReturn("uw9fhew");
		assertNotNull(service.registerNewUserAccount(user));
	}

	@Test(expected = EmailExistsException.class)
	public void testRegisterNewUserAccountException() throws EmailExistsException {
		User user = new User();
		user.setEmail("aaa");
		given(dao.findByEmail(anyString())).willReturn(user);
		given(dao.existsById(anyLong())).willReturn(true);
		assertNotNull(service.registerNewUserAccount(user));
	}

	@Test
	public void testGetAllUsers() {
		given(dao.findByOrderByName(pageRequest)).willReturn(Page.empty());
		assertNotNull(service.getAllUsers(pageRequest));
	}

	@Test
	public void testGetUserById() throws UserNotFoundException {
		given(dao.findById(anyLong())).willReturn(Optional.of(new User()));
		assertNotNull(service.getUser(anyLong()));
	}

	@Test
	public void testGetUserByEmail() {
		given(dao.findByEmail(anyString())).willReturn(new User());
		assertNotNull(service.getUserByEmail(anyString()));
	}

	@Test
	public void testGetUserByParam() {
		given(dao.findByParam(anyString(), eq(pageRequest))).willReturn(Page.empty());
		assertNotNull(service.getUsersByParam(anyString(), eq(pageRequest)));
	}

	@Test
	public void testGetUserByParamNull() {
		assertNull(service.getUsersByParam(any(), pageRequest));
	}

	@Test
	public void testEmailExists() {
		given(dao.findByEmail(anyString())).willReturn(new User());
		given(dao.existsById(anyLong())).willReturn(true);
		assertTrue(service.emailExist("Prueba"));
	}

	@Test
	public void testEmailNotExists() {
		given(dao.findByEmail(anyString())).willReturn(null);
		assertFalse(service.emailExist("Prueba"));
	}

	@Test(expected = UserNotFoundException.class)
	public void testUserNotFound() throws UserNotFoundException {
		given(dao.findById(anyLong())).willReturn(Optional.ofNullable(null));
		assertNull(service.getUser(anyLong()));
	}

	@Test
	public void testConfirmUser() throws UserNotFoundException {
		User user = new User();
		given(dao.findById(anyLong())).willReturn(Optional.ofNullable(user));
		given(dao.save(user)).willReturn(user);
		assertNotNull(service.confirmUser(anyLong()));
	}

	@Test(expected = UserNotFoundException.class)
	public void testConfirmUserUserNotFound() throws UserNotFoundException {
		given(dao.findById(anyLong())).willReturn(Optional.ofNullable(null));
		assertNotNull(service.confirmUser(anyLong()));
	}

	@Test
	public void testDeleteUser() {
		User user = new User();
		dao.save(user);
		long id = user.getId();
		service.deleteUser(id);
		assertFalse(dao.existsById(id));
	}

	@Test
	public void testUpdateUser() throws UserNotFoundException {
		User user = new User();
		user.setName("");
		user.setSurname("");
		user.setId(1);
		User userToUpdate = new User();
		given(dao.findById(anyLong())).willReturn(Optional.ofNullable(userToUpdate));
		given(dao.save(userToUpdate)).willReturn(userToUpdate);
		assertNotNull(service.updateUser(user));
	}

	@Test(expected = UserNotFoundException.class)
	public void testUpdateUserException() throws UserNotFoundException {
		User user = new User();
		user.setId(1);
		given(dao.findById(anyLong())).willReturn(Optional.ofNullable(null));
		assertNotNull(service.updateUser(user));
	}
}
