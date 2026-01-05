package entities;

public class GetHoursDayRequest extends Request{
	private static final long serialVersionUID = 1L;

	public GetHoursDayRequest() {
		super(RequestType.GET_HOURS_DAY, "SELECT * FROM `day`;");
	}
}
