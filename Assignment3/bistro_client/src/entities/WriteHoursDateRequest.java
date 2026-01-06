package entities;

public class WriteHoursDateRequest extends Request{
	private static final long serialVersionUID = 1L;
	private String date;
	private String open;
	private String close;
	
	public WriteHoursDateRequest(String date, String open, String close) {
		super(RequestType.WRITE_HOURS_DATE,
				"INSERT INTO `date`" + 
				"(specific_date, open_hour, close_hour)" +
				"VALUES (?, ?, ?)"
		);
		this.date = date;
		this.open = open;
		this.close = close;
	}
	
	public String getDate() {
		return date;
	}
	
	public String getOpen() {
		return open;
	}
	
	public String getClose() {
		return close;
	}
}
