package entities.requests;

/**
 * Represents a request to try to seat a customer based on a confirmation code.
 */
public class TrySeatRequest extends Request {
	private int confCode;
	
	/**
	 * Gets the confirmation code associated with this request.
	 * 
	 * @return the confirmation code
	 */
	public TrySeatRequest() {
		super(RequestType.TRY_SEAT, "SELECT * FROM `order` WHERE status = 'OPEN' AND confirmation_code = ?;");
		
	}

}
