package tfg.microservice.user.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import tfg.microservice.user.model.User;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private UserRepository users;

	@Before
	public void setUp() {
		String string = "Prueba";
		User user = new User(string, string, string, string, string, string);
		user.setPassword("pass");
		entityManager.persist(user);
		entityManager.flush();
	}

	@Test
	public void findByEmail() {
		User found = users.findByEmail("Prueba");
		assertTrue(found.getEmail().equals("Prueba"));
	}

	@Test
	public void findAll() {
		assertNotNull(users.findAll());
	}

	@Test
	public void findById() {
		assertNotNull(users.findById((long) 1));
	}

	@Test
	public void findByParam() {
		assertNotNull(users.findByParam("Prueba", PageRequest.of(1, 1)));
	}

	@Test
	public void findByOrderByName() {
		assertNotNull(users.findByOrderByName(PageRequest.of(1, 1)));
	}
}
