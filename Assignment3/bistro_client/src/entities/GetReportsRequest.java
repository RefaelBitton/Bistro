package entities;

/**
 * Represents a request to retrieve reports from the server. This request does
 * not require any query parameters as the server handles the logic for fetching
 * reports internally.
 */
public class GetReportsRequest extends Request {
    private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new GetReportsRequest.
	 */
    public GetReportsRequest() {
        // No query string needed, the server handles the complex logic
        super(RequestType.GET_REPORTS, "");
    }
}