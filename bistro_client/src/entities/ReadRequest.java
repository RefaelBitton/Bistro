package entities;

public class ReadRequest extends Request {

	public ReadRequest(Order order) {
		super(order, RequestType.READ, "SELECT * FROM `order` WHERE order_number = ?");
	}

}
