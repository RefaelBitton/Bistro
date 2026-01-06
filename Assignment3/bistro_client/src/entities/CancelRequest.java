package entities;

public class CancelRequest extends Request {
	private static final long serialVersionUID = 1L;
	private String code;
	public CancelRequest(String code) {
		super(RequestType.CANCEL_REQUEST, "UPDATE `order` SET status = 'CANCELLED' WHERE confirmation_code = ?");
		this.code = code;
	}
	public String getCode() {
		return code;
	}
}
