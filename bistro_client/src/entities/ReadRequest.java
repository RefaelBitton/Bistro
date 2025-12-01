package entities;

public class ReadRequest extends Request {

	private static final long serialVersionUID = 1L;

	public ReadRequest(Order order) {
		super(order, RequestType.READ, "SELECT * FROM `order` WHERE order_number = ?");
	}

}
