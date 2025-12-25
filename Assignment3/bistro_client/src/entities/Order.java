package entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    private String orderNumber;         // from GET_NEXT_ORDER_NUMBER
    private String confirmationCode;    // computed from orderNumber

    private String orderDateTime;       // yyyy-MM-dd HH:mm:ss
    private String numberOfGuests;
    private String subscriberId;        // "0" means guest
    private String dateOfPlacingOrder;  // yyyy-MM-dd
    private String contact;             // phone/email or subscriber email

    // args:
    // 0 date (yyyy-MM-dd)
    // 1 time (HH:mm)
    // 2 guests
    // 3 subscriberId
    // 4 contact
    // 5 orderNumber
    // 6 confirmationCode
    public Order(ArrayList<String> args) {
    	this.orderNumber = args.get(0);
        String date = args.get(1);
        String time = args.get(2);
        this.orderDateTime = date + " " + time + ":00";

        this.numberOfGuests = args.get(3);
        this.confirmationCode = args.get(4);
        this.subscriberId = args.get(5);
        this.dateOfPlacingOrder = LocalDate.now().toString();
        this.contact = args.get(6);
        

    }

    public String getOrderNumber() { return orderNumber; }
    public String getConfirmationCode() { return confirmationCode; }

    public String getOrderDateTime() { return orderDateTime; }
    public String getNumberOfGuests() { return numberOfGuests; }
    public String getSubscriberId() { return subscriberId; }
    public String getDateOfPlacingOrder() { return dateOfPlacingOrder; }
    public String getContact() { return contact; }
}
