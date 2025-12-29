package entities;

public class TrySeatRequest extends Request {
	private int confCode;
	public TrySeatRequest() {
		super(RequestType.TRY_SEAT, "SELECT * FROM `order` WHERE status = 'OPEN' AND confirmation_code = ?;");
		
	}

}
