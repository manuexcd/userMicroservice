package tfg.microservice.user.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import tfg.microservice.user.dto.UserDTO;
import tfg.microservice.user.exception.EmailExistsException;
import tfg.microservice.user.exception.UserNotFoundException;
import tfg.microservice.user.mapper.UserMapper;
import tfg.microservice.user.model.User;
import tfg.microservice.user.service.UserService;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

	private MockMvc mvc;

	@Mock
	private UserService service;

	@Mock
	private UserMapper mapper;

	@InjectMocks
	private UserController controller;

	@Before
	public void setup() {
		mvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	public void getAllUsers() throws Exception {
		mvc.perform(get("/users").contentType(APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().string(containsString("")));
	}

	@Test
	public void getUserById() throws Exception {
		User user = new User("user", "pass");
		given(service.getUser(anyLong())).willReturn(user);
		mvc.perform(get("/users/1").contentType(APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void getUserByIdNotFound() throws Exception {
		given(service.getUser(anyLong())).willReturn(null);
		mvc.perform(get("/users/1").contentType(APPLICATION_JSON)).andExpect(status().isNotFound());
	}

	@Test
	public void getUserByEmail() throws Exception {
		User user = new User("user", "pass");
		user.setSurname("surname");
		given(service.getUserByEmail(anyString())).willReturn(user);
		mvc.perform(get("/users/email/a").contentType(APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void getUserByEmailNotFound() throws Exception {
		given(service.getUserByEmail(anyString())).willReturn(null);
		mvc.perform(get("/users/email/a").contentType(APPLICATION_JSON)).andExpect(status().isNotFound());
	}

	@Test
	public void getUsersByParam() throws Exception {
		mvc.perform(get("/users/search/a").contentType(APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void testRegisterUser() throws Exception {
		User user = new User("user", "pass");
		user.setName("name");
		user.setSurname("surname");
		String body = "{\n	\"email\":\"user\",\n	\"password\":\"pass\"\n}";
		given(service.registerNewUserAccount(any())).willReturn(user);
		given(mapper.mapDtoToEntity(any())).willReturn(user);
		mvc.perform(post("/users/register").content(body).contentType(APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful());
	}

	@Test
	public void testRegisterUserException() throws Exception {
		User user = new User("user", "pass");
		user.setAddress("prueba");
		user.setName("name");
		user.setSurname("surname");
		String body = "{\n	\"email\":\"manuexcd@gmail.com\",\n	\"password\":\"pass\"\n}";
		given(service.registerNewUserAccount(any())).willThrow(EmailExistsException.class);
		mvc.perform(post("/users/register").content(body).contentType(APPLICATION_JSON))
				.andExpect(status().is5xxServerError());
	}

	@Test
	public void testConfirmUser() throws Exception {
		mvc.perform(post("/users/confirm/1").contentType(APPLICATION_JSON)).andExpect(status().is2xxSuccessful());
	}

	@Test
	public void testConfirmUserException() throws Exception {
		given(service.confirmUser(anyLong())).willThrow(UserNotFoundException.class);
		mvc.perform(post("/users/confirm/1").contentType(APPLICATION_JSON)).andExpect(status().isNotFound());
	}

	@Test
	public void testUpdateUser() throws Exception {
		ObjectMapper obj = new ObjectMapper();
		mvc.perform(put("/users/1").content(obj.writeValueAsString(new UserDTO())).contentType(APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful());
	}

	@Test
	public void testUpdateUserException() throws Exception {
		ObjectMapper obj = new ObjectMapper();
		given(service.getUser(anyLong())).willThrow(UserNotFoundException.class);
		mvc.perform(put("/users/1").content(obj.writeValueAsString(new UserDTO())).contentType(APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testUploadImage() throws Exception {
		MockMultipartFile file = new MockMultipartFile("data", "filename.txt", MediaType.MULTIPART_FORM_DATA_VALUE,
				"some xml".getBytes());
		given(service.addImage(any())).willReturn(anyString());
		mvc.perform(MockMvcRequestBuilders.multipart("/users/image").file("file", file.getBytes()))
				.andExpect(status().isOk());
	}
}
