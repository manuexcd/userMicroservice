package tfg.microservice.user;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserApplicationTests {

	@Test
	public void main() {
		UserApplication.main(new String[] {});
		assertTrue(true);
	}

	@Test
	void contextLoads() {
		assertTrue(true);
	}
}
