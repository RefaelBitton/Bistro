package entities;

public class Order {
	private int orderNumber;
	private String orderDate;
	private int numberOfGuests;
	private int confirmationCode;
	private int subscriberId;
	private String dateOfPlacingOrder;
	
	public Order(int orderNumber,String orderDate, int numberOfGuests, int confirmationCode, int subscriberId, String dateOfPlacingOrder) {
		this.confirmationCode = confirmationCode;
		this.dateOfPlacingOrder = dateOfPlacingOrder;
		this.numberOfGuests = numberOfGuests;
		this.orderDate = orderDate;
		this.orderNumber = orderNumber;
		this.subscriberId = subscriberId;
	}
	
	//Getters
	public int getOrderNumber() {
		return orderNumber;
	}
	
	public String getOrderDate() {
		return orderDate;
	}
	
	public int getNumberOfGuests() {
		return numberOfGuests;
		}
	
	public int getConfirmationCode() {
		return confirmationCode;
	}
	
	public int getSubscriberId() {
		return subscriberId;
	}
	
	public String getDateOfPlacingOrder() {
		return dateOfPlacingOrder;
	}
	
	
	
//	//Setters
//	public void setOrderNumber(int num) {
//		orderNumber = num;
//	}
//	
//	public void setOrderDate(String date) {
//		orderDate = date;
//	}
//	
//	public void setNumberOfGuests(int num) {
//		numberOfGuests = num;
//	}
//	
//	public void setConfirmationCode(int num) {
//		confirmationCode = num;
//	}
//	
//	public void setSubscriberId(int id) {
//		subscriberId = id;
//	}
//	
//	public void setDateOfPlacingOrder(String date) {
//		dateOfPlacingOrder = date;
//	}
	
	
	
}
