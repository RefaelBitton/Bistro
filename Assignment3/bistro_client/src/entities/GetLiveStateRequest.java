package entities;

public class GetLiveStateRequest extends Request {
	private static final long serialVersionUID = 1L;

    public GetLiveStateRequest() {
        // We pass empty string because we don't need a SQL query, 
        // we are accessing Server Memory objects.
        super(RequestType.GET_LIVE_BISTRO_STATE, "");
    }
}
