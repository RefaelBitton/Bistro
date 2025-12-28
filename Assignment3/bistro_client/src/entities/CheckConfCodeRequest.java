package entities;

public class CheckConfCodeRequest extends Request {
	private static final long serialVersionUID = 1L;
	private String contact;
	public CheckConfCodeRequest(String contact) { //only primary key (order number) needed for reading details about existing order 
		super(RequestType.CHECK_CONFCODE, "SELECT confirmation_code FROM `order` WHERE contact = ? AND order_datetime BETWEEN "
				+ "DATE_SUB(?, INTERVAL 30 MINUTE) AND DATE_ADD(?, INTERVAL 30 MINUTE)");
				
		this.contact=contact;
	}
	/**
	 * 
	 * @return the order number for that request
	 */
	public String getcontact() {
		return contact;
	}
	


}
