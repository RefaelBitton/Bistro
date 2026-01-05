package entities;

import java.io.Serializable;
import java.sql.Time;
import java.time.LocalDate;

public class SpecificDate implements Serializable{
	private static final long serialVersionUID = 1L;
	private LocalDate date;
	private Time open;
	private Time close;
	
	public SpecificDate(LocalDate date, Time open, Time close) {
		this.date = date;
		this.open = open;
		this.close = close;
	}
	
	public LocalDate getDate() {
		return this.date;
	}
	
	public Time getOpen() {
		return this.open;
	}
	
	public Time getClose() {
		return this.close;
	}
}
