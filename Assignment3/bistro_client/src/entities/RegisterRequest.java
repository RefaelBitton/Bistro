package entities;
/**
 * A request that handles a new registration of a user
 */
public class RegisterRequest extends Request {

	private static final long serialVersionUID = 1L;
	private Subscriber user;
	public RegisterRequest(Subscriber user) {
		super(RequestType.REGISTER_REQUEST, "INSERT INTO `user` (full_name, subscriber_id, username, phone_number, email)\n"
				+ "VALUES (?, ?, ?, ?, ?);");
		this.user = user;
	}
	public Subscriber getUser() {
		return user;
	}
	

}
