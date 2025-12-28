package entities;

public class GetAllSubscribersRequest extends Request {

	private static final long serialVersionUID = 1L;

	public GetAllSubscribersRequest() {
		super(RequestType.GET_ALL_SUBSCRIBERS, "SELECT * FROM `user` WHERE `status` = `SUBSCRIBER`;");
	}

}
