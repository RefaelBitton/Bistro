package entities;

public class WriteRequest extends Request {

	public WriteRequest(Order order) {
		super(order,RequestType.WRITE ,
        	    "INSERT INTO `order` (order_number, order_date, number_of_guests, confirmation_code, subscriber_id, date_of_placing_order) "
        	  + "VALUES(?, ?, ?, ?, ?, ?);");
	}

}
