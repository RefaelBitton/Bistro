package entities;

public class ReadRequest extends Request {

	private static final long serialVersionUID = 1L;
	private String orderNum;
	public ReadRequest(String orderNum) {
		super(RequestType.READ, "SELECT * FROM `order` WHERE order_number = ?");
		this.orderNum = orderNum;
	}
	
	public String getOrderNum() {
		return orderNum;
	}
	

}
