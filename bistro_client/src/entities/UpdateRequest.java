package entities;

public class UpdateRequest extends Request {
	
	private static final long serialVersionUID = 1L;
	private String orderNum;
	private int guestsNum;
	private String date;

	public UpdateRequest(String orderNum, int guestsNum) {
		super(RequestType.UPDATE_GUESTS,"UPDATE `order` SET number_of_guests = ? WHERE order_number = ?");
		this.orderNum = orderNum;
		this.guestsNum = guestsNum;
	}
	
	public UpdateRequest(String orderNum, String date) {
		super(RequestType.UPDATE_DATE,"UPDATE `order` SET order_date = ? WHERE order_number = ?");
		this.orderNum = orderNum;
		this.date = date;
	}
	
	public String getOrderNum() {
		return orderNum;
	}
	
	public int getNumberOfGuests() {
		return guestsNum;
	}
	
	public String getDate() {
		return date;
	}
}
