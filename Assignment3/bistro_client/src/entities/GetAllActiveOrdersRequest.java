package entities;

public class GetAllActiveOrdersRequest extends Request {

	private static final long serialVersionUID = 1L;

	public GetAllActiveOrdersRequest() {
		super(RequestType.GET_ALL_ACTIVE_ORDERS, "SELECT * FROM `order` WHERE status = 'OPEN'");
	}

}
