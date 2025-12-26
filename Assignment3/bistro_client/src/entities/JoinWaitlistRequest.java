package entities;

/**
 * A concrete implementation of Request for joining the waitlist.
 */
public class JoinWaitlistRequest extends Request {
    private static final long serialVersionUID = 1L;
    private Order order;

    public JoinWaitlistRequest(Order order) {
        // You can leave the query string empty if the server handles logic internally
        super(RequestType.JOIN_WAITLIST, ""); 
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}