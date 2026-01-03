package entities;

/**
 * A concrete implementation of Request for joining the waitlist.
 */
public class JoinWaitlistRequest extends Request {
    private static final long serialVersionUID = 1L;
    private String orderDateTime;
    private String numberOfGuests;
    private String subscriberId;
    private String contact;
    private boolean isWaitlistEntry = false;
    
    public JoinWaitlistRequest(String orderDateTime, String numberOfGuests, String subscriberId, String contact) {
        // You can leave the query string empty if the server handles logic internally
        super(RequestType.JOIN_WAITLIST, ""); 
        this.orderDateTime = orderDateTime;
        this.numberOfGuests = numberOfGuests;
        this.subscriberId = subscriberId;
        this.contact = contact;
    }
    
 // New constructor for the second attempt (waitlist confirmation)
    public JoinWaitlistRequest(String orderDateTime, String numberOfGuests, String subscriberId, String contact, boolean isWaitlistEntry) {
        this(orderDateTime, numberOfGuests, subscriberId, contact); // Calls existing constructor
        setWaitlistEntry(isWaitlistEntry);
    }
    
    public boolean isWaitlistEntry() { return isWaitlistEntry; }
    public void setWaitlistEntry(boolean isWaitlistEntry) { this.isWaitlistEntry = isWaitlistEntry; }
    public String getOrderDateTime() { return orderDateTime; }
    public String getNumberOfGuests() { return numberOfGuests; }
    public String getSubscriberId() { return subscriberId; }
    public String getContact() { return contact; }

}