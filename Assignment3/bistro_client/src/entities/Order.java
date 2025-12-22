package entities;

import java.io.Serializable;
import java.util.ArrayList;

public class Order implements Serializable {
	private static final long serialVersionUID = 1L;
	private String orderNumber;
	private String orderDate;
	private String numberOfGuests;
	private String confirmationCode;
	private String subscriberId;
	private String dateOfPlacingOrder;
	
	/**
	 * @param args an array of arguments for defining an order
	 * [0] - orderNumber,
	 * [1] - orderDate,
	 * [2] - numberOfGuests,
	 * [3] - confirmaionCode,
	 * [4] - subscriberId,
	 * [5] - dateOfPlacingOrder
	 * */
	public Order(ArrayList<String> args) {
		this.orderNumber = args.get(0);
		this.orderDate = args.get(1);
		this.numberOfGuests = args.get(2);
		this.confirmationCode = args.get(3);
		this.subscriberId = args.get(4);
		this.dateOfPlacingOrder = args.get(5);
	}
	
	/**
	 * 
	 * @return the order number
	 */
	public String getOrderNumber() {
		return orderNumber;
	}
	/**
	 * 
	 * @return the order's date
	 */
	public String getOrderDate() {
		return orderDate;
	}
	/**
	 * 
	 * @return the number of guests
	 */
	public String getNumberOfGuests() {
		return numberOfGuests;
		}
	/**
	 * 
	 * @return the order's confirmation code
	 */
	public String getConfirmationCode() {
		return confirmationCode;
	}
	/**
	 * 
	 * @return the user's id that ordered
	 */
	public String getSubscriberId() {
		return subscriberId;
	}
	/**
	 * 
	 * @return the date of placing the order
	 */
	public String getDateOfPlacingOrder() {
		return dateOfPlacingOrder;
	}
	@Override
	public String toString() {
		return "Order Number: "+getOrderNumber()+
				"\nOrder Date: "+getOrderDate()+
				"\nNumber Of Guests: " +getNumberOfGuests()+
				"\nConfirmation Code: " +getConfirmationCode()+
				"\nSubscriber ID: " + getSubscriberId() +
				"\nDate of placing the order: " + getDateOfPlacingOrder();
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
