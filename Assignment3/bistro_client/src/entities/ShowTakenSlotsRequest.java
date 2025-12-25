package entities;
/**
 * a class that represents a request to show taken slots for a given number of guests and date/time
 * */
public class ShowTakenSlotsRequest extends Request {

    private static final String Q =
            "SELECT number_of_guests FROM `order` WHERE status = 'OPEN' AND order_datetime BETWEEN ? AND ?;";

    private static final long serialVersionUID = 1L;
    private int numberOfGuests;
    private String orderDateTime;
    public ShowTakenSlotsRequest(int numberOfGuests, String orderDateTime) {
        super(RequestType.GET_TAKEN_SLOTS, Q);
        this.numberOfGuests = numberOfGuests;
        this.orderDateTime = orderDateTime;
    }

    public int getNumberOfGuests() {
		return numberOfGuests;
	}
    
    public String getOrderDateTime() {
		return orderDateTime;
	}

}
