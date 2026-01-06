package entities;

public class GetReportsRequest extends Request {
    private static final long serialVersionUID = 1L;

    public GetReportsRequest() {
        // No query string needed, the server handles the complex logic
        super(RequestType.GET_REPORTS, "");
    }
}