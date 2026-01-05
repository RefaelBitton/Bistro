package entities;

import java.io.Serializable;
import java.sql.Time;

public class Day implements Serializable{
	private static final long serialVersionUID = 1L;
	private int day;
	private Time open;
	private Time close;
	
	public Day(int day, Time open, Time close) {
		this.day = day;
		this.open = open;
		this.close = close;
	}
	
	public int getDay() {
		return this.day;
	}
	
	public Time getOpen() {
		return this.open;
	}
	
	public Time getClose() {
		return this.close;
	}
}
