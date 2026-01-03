package entities;

public class LeaveTableRequest extends Request {

	private static final long serialVersionUID = 1L;
	private String confCode;

	public LeaveTableRequest(String confCode) {
		super(RequestType.LEAVE_TABLE, "SELECT subscriber_id, status, order_number FROM `order` WHERE confirmation_code = ?");
		this.confCode = confCode;
	}
	
	public String getConfCode() {
		return confCode;
	}

}
