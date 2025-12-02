package entities;

public class WriteRequest extends Request {

	private static final long serialVersionUID = 1L;
	private Order order;
	public WriteRequest(Order order) {
		super(RequestType.WRITE ,
        	    "INSERT INTO `order` (order_number, order_date, number_of_guests, confirmation_code, subscriber_id, date_of_placing_order) "
        	  + "VALUES(?, ?, ?, ?, ?, ?);");
		this.order = order;
	}
	public Order getOrder() {
		return order;
	}

}
