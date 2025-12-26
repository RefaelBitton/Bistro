package entities;

public class CancelRequest extends Request {
	private static final long serialVersionUID = 1L;
	private String orderNum;
	private String code;
	public CancelRequest(String orderNum, String code) {
		super(RequestType.CANCEL_REQUEST, "UPDATE `order` SET status = 'CANCELLED' WHERE order_number = ? AND confirmation_code = ?");
		this.orderNum = orderNum;
		this.code = code;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public String getCode() {
		return code;
	}
}
