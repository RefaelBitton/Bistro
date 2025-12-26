package entities;

/**
 * A concrete implementation of Request for removing a customer from the waiting list.
 */
public class LeaveWaitlistRequest extends Request {
    private static final long serialVersionUID = 1L;
    private String orderNum;

    /**
     * @param orderNum The unique identifier for the waitlist entry to be removed.
     */
    public LeaveWaitlistRequest(String orderNum) {
        // We pass the RequestType and an empty query as the logic is handled by the DLL in the server.
        super(RequestType.LEAVE_WAITLIST, "");
        this.orderNum = orderNum;
    }

    public String getOrderNum() {
        return orderNum;
    }
}