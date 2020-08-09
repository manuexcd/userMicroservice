package tfg.microservice.user.model;

public final class Constants {
	private Constants() {
	}

	public static final String PAGINATION_DEFAULT_PAGE = "0";
	public static final String PAGINATION_DEFAULT_SIZE = "5";

	public static final String PATH_USERS = "/users";
	public static final String PATH_EMAIL = "/email";
	public static final String PATH_SEARCH = "/search";
	public static final String PATH_REGISTER = "/register";
	public static final String PATH_CONFIRM = "/confirm";

	public static final String PARAM_ID = "/{id}";
	public static final String PARAM_EMAIL = "/{email}";
	public static final String PARAM = "/{param}";

	public static final String SUBJECT_USER_REGISTERED = "Registro completado";
	public static final String SUBJECT_USER_UPDATED = "Perfil de usuario actualizado";

	public static final String TEMPLATE_PARAM_FULLNAME = "fullName";
	public static final String TEMPLATE_PARAM_PHONE = "phone";
	public static final String TEMPLATE_PARAM_EMAIL = "email";
	public static final String TEMPLATE_PARAM_ADDRESS = "address";

	public static final String TEMPLATE_USER_REGISTERED = "Se ha completado el registro para el usuario {{ fullName }}. Los datos del usuario son:\n"
			+ "\t Nombre: {{ fullName }}\n" + "\t Teléfono: {{ phone }}\n" + "\t Email: {{ email }}\n"
			+ "\t Dirección: {{ address }}";
	public static final String TEMPLATE_USER_UPDATED = "Se ha actualizado el perfil del usuario {{ fullName }}.\n"
			+ "Los nuevos datos del usuario son:\n" + "\t Nombre: {{ fullName }}\n" + "\t Teléfono: {{ phone }}\n"
			+ "\t Email: {{ email }}\n" + "\t Dirección: {{ address }}";
}
