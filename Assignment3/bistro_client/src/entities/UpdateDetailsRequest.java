package entities;

public class UpdateDetailsRequest extends Request {

	private static final long serialVersionUID = 1L;

	public UpdateDetailsRequest(String query) {
		super(RequestType.UPDATE_DETAILS, query);
	}

}
