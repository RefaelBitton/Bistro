package entities;

public class ChangeHoursDayRequest extends Request{
	private static final long serialVersionUID = 1L;
	private String day;
	private String open;
	private String close;
	
	public ChangeHoursDayRequest(String day, String open, String close) {
		super(RequestType.CHANGE_HOURS_DAY, "UPDATE `day` SET open_hour = ? , close_hour = ? WHERE day_of_week = ?");
		this.day = day;
		this.open = open;
		this.close = close;
	}
	
	public String getDay() {
		return day;
	}
	
	public String getOpen() {
		return open;
	}
	
	public String getClose() {
		return close;
	}
}
