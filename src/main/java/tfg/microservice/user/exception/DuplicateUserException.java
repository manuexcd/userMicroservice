package tfg.microservice.user.exception;

public class DuplicateUserException extends Exception {
	
	private static final long serialVersionUID = 7319980745274300196L;

	private static final String MSG = "Duplicate user";

	public DuplicateUserException() {
		super(MSG);
	}
}
