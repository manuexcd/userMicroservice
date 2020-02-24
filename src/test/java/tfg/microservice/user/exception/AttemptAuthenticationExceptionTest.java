package tfg.microservice.user.exception;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AttemptAuthenticationExceptionTest {
	@Test
	public void duplicateUserException() {
		assertNotNull(new AttemptAuthenticationException());
	}
}
