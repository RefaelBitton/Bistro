package entities;

public class LoginRequest extends Request {
	private static final long serialVersionUID = 1L;
	private int subscriberId;
	public LoginRequest(int subscriberId) {
		super(RequestType.LOGIN_REQUEST, "SELECT EXISTS(SELECT 1 FROM `user` WHERE subscriber_id = ?);");
		this.subscriberId = subscriberId;
	}
	public int getId() {
		return subscriberId;
	}

}
