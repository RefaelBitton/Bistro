package entities;

public class CheckSlotRequest extends Request {

    private static final String Q =
            "SELECT 1 FROM `order` WHERE order_datetime = ? LIMIT 1";

    private final String orderDateTime; 
    private static final long serialVersionUID = 1L;

    public CheckSlotRequest(String orderDateTime) {
        super(RequestType.CHECK_SLOT, Q);
        this.orderDateTime = orderDateTime;
    }

    public String getOrderDateTime() {
        return orderDateTime;
    }
}
