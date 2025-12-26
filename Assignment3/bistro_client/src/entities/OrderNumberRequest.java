package entities;

public class OrderNumberRequest extends Request {
	private static final long serialVersionUID = 1L;
    private static final String Q =
            "SELECT IFNULL(MAX(order_number), 0) + 1 AS next_num FROM `order`";

    public OrderNumberRequest() {
        super(RequestType.ORDER_NUMBER, Q);
    }
}