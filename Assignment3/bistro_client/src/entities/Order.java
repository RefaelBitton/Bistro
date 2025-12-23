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
    public String getNumberOfGuests() { return numberOfGuests; }
    public String getSubscriberId() { return subscriberId; }
    public String getDateOfPlacingOrder() { return dateOfPlacingOrder; }
    public String getContact() { return contact; }
}
