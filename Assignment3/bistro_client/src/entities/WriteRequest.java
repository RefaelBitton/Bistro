package entities;

public class WriteRequest extends Request {

	private static final long serialVersionUID = 1L;
	private Order order;
	public WriteRequest(Order order) {
	    super(
	        RequestType.WRITE_ORDER,
	        "INSERT INTO `order` " +
	        "(order_number, order_datetime, number_of_guests, confirmation_code, subscriber_id, date_of_placing_order, contact) " +
	        "VALUES (?, ?, ?, ?, ?, ?, ?)"
	    );
	    this.order = order;
	}
	
	public Order getOrder() {
		return order;
	}

}
