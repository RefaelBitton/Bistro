package entities;

public class ReadRequest extends Request {

	private static final long serialVersionUID = 1L; //id number that represent the version of the class in serialize time 
	private String orderNum;
	public ReadRequest(String orderNum) { //only primary key (order number) needed for reading details about existing order 
		super(RequestType.READ_ORDER, "SELECT * FROM `order` WHERE order_number = ?");
		this.orderNum = orderNum;
	}
	
	public String getOrderNum() {
		return orderNum;
	}
	

}
