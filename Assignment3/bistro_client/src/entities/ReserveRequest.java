package entities;

public class ReserveRequest extends WriteRequest {

	private static final long serialVersionUID = 1L;

	public ReserveRequest(String orderDateTime, String numberOfGuests, String subscriberId, String contact) {
		super(orderDateTime, numberOfGuests, subscriberId, contact);
		this.setType(RequestType.RESERVE_TABLE);
	}
}
