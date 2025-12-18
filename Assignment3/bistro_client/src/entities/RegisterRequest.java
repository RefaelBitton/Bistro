package entities;

public class RegisterRequest extends Request {

	private static final long serialVersionUID = 1L;
	private User user;
	public RegisterRequest(User user) {
		super(RequestType.REGISTER_REQUEST, "INSERT INTO `user` (full_name, subscriber_id, username, phone_number, email)\n"
				+ "VALUES (?, ?, ?, ?, ?);");
		this.user = user;
	}
	public User getUser() {
		return user;
	}
	

}
