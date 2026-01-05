package entities;

public class GetHoursDateRequest extends Request {

	private static final long serialVersionUID = 1L;

	public GetHoursDateRequest() {
		super(RequestType.GET_HOURS_DATE, "SELECT * FROM `date`;");
	}
}
