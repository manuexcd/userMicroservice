package tfg.microservice.user.model;

public final class Constants {
	private Constants() {
	}

	public static final String PAGINATION_DEFAULT_PAGE = "0";
	public static final String PAGINATION_DEFAULT_SIZE = "5";

	public static final String PATH_PRODUCTS = "/products";
	public static final String PATH_USERS = "/users";
	public static final String PATH_ORDERS = "/orders";
	public static final String PATH_ORDERLINES = "/orderLines";
	public static final String PATH_EMAIL = "/email";
	public static final String PATH_SEARCH = "/search";
	public static final String PATH_SIGN_IN = "/signin";
	public static final String PATH_LOGIN_DETAILS = "/logindetails";

	public static final String PARAM_ID = "/{id}";
	public static final String PARAM_EMAIL = "/{email}";
	public static final String PARAM = "/{param}";
	public static final String PARAM_ORDER_ID = "/{orderId}";

	public static final String ORDER_STATUS_TEMPORAL = "TEMPORAL";
	public static final String ORDER_STATUS_RECEIVED = "RECEIVED";
	public static final String ORDER_STATUS_IN_PROGRESS = "IN_PROGRESS";
	public static final String ORDER_STATUS_IN_DELIVERY = "IN_DELIVERY";
	public static final String ORDER_STATUS_DELIVERED = "DELIVERED";
	public static final String ORDER_STATUS_CANCELLED = "CANCELLED";

	public static final String SUBJECT_ORDER_CONFIRMED = "Pedido confirmado";
	public static final String SUBJECT_ORDER_UPDATED = "Pedido modificado";
	public static final String SUBJECT_ORDER_CANCELLED = "Pedido cancelado";
	public static final String SUBJECT_USER_REGISTERED = "Registro completado";

	public static final String TEMPLATE_PARAM_ORDER_ID = "orderId";
	public static final String TEMPLATE_PARAM_ORDER_STATUS = "orderStatus";
	public static final String TEMPLATE_PARAM_ORDER_PRICE = "orderPrice";
	public static final String TEMPLATE_PARAM_ORDER_LINES = "orderLines";
	public static final String TEMPLATE_PARAM_FULLNAME = "fullName";

	public static final String TEMPLATE_ORDER_CONFIRMED = "Se ha confirmado el pedido {{orderId}}.\nImporte del pedido: {{orderPrice}} €.";
	public static final String TEMPLATE_ORDER_UPDATED = "Se ha modificado el pedido {{orderId}}. Estado del pedido: {{orderStatus}}.";
	public static final String TEMPLATE_ORDER_CANCELLED = "Se ha cancelado el pedido {{orderId}}.";
	public static final String TEMPLATE_USER_REGISTERED = "Se ha completado el registro para el usuario {{fullName}}.";

	public static final String GOOGLE_CLOUD_PROJECT_ID = "tfg-kubernetes-250608";
	public static final String GOOGLE_CLOUD_BUCKET_NAME = "tfg-images";
}
