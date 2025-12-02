package entities;

import java.io.Serializable;
public abstract class Request implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private RequestType type;
	private String query;
	
	
	public Request(RequestType type, String query) {
		this.type = type;
		this.query = query;
	}
	
	public String getQuery() {
		return query;
	}
	
	public RequestType getType() {
		return type;
	}
}
