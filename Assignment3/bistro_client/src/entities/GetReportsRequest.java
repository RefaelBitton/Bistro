package entities;

import java.time.Month;

/**
 * Represents a request to retrieve reports from the server. This request does
 * not require any query parameters as the server handles the logic for fetching
 * reports internally.
 */
public class GetReportsRequest extends Request {
    private static final long serialVersionUID = 1L;
    private Month month;
    private int year;
	/**
	 * Constructs a new GetReportsRequest.
	 */
    public GetReportsRequest(Month month,int year) {
        super(RequestType.GET_REPORTS, "");
        this.month = month;
        this.year = year;
    }
    
    public Month getMonth() {
    	return month;
    }
    
    public int getYear() {
    	return year;
    }
}