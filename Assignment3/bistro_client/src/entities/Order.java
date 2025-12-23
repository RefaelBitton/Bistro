package entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    private String orderDateTime;
    private String numberOfGuests;
    private String subscriberId;        // "0" from client means guest
    private String dateOfPlacingOrder;  // today
    private String contact;             // guest phone/email OR subscriber email (after READ_EMAIL)

    public Order(ArrayList<String> args) {
    	String date = args.get(0); 
    	String time = args.get(1); 
    	this.orderDateTime = date + " " + time + ":00";

        this.numberOfGuests = args.get(2);
        this.subscriberId = args.get(3);
        this.contact = args.get(4);
        this.dateOfPlacingOrder = LocalDate.now().toString();
    }

    public String getOrderDateTime() { return orderDateTime; }
    public String getContact() { return contact; }
	
	/**
	 * 
	 * @return the number of guests
	 */
	public String getNumberOfGuests() {
		return numberOfGuests;
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
/*	@Override
	public String toString() {
		return "Order Number: "+getOrderNumber()+
				"\nOrder Date: "+getOrderDate()+
				"\nNumber Of Guests: " +getNumberOfGuests()+
				"\nConfirmation Code: " +getConfirmationCode()+
				"\nSubscriber ID: " + getSubscriberId() +
				"\nDate of placing the order: " + getDateOfPlacingOrder();
	}
	*/
	
	
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
