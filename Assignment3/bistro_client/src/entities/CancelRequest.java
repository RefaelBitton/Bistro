package entities;

public class CancelRequest extends Request {
	private static final long serialVersionUID = 1L;
	private String orderNum;
	public CancelRequest(String orderNum) {
		super(RequestType.CANCEL_REQUEST, "DELETE FROM `order` WHERE order_number = ?");
		this.orderNum = orderNum;
	}
	public String getOrderNum() {
		return orderNum;
	}
}
