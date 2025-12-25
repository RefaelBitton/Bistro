package entities;

public class ShowOpenSlotsRequest extends Request {

    private static final String Q =
            "SELECT order_datetime FROM `order` WHERE order_datetime BETWEEN ? AND ?";

    private final String from;
    private final String to;
    private static final long serialVersionUID = 1L;

    public ShowOpenSlotsRequest(String from, String to) {
        super(RequestType.SHOW_OPEN_SLOTS, Q);
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}
