package entities;

import java.io.Serializable;
//TODO: maybe doesn't need Request class because read only needs the order number and not the whole order
public abstract class Request implements Serializable {
	private static final long serialVersionUID = 1L;
	private Order order;
	private RequestType type;
	private String query;
	
	
	public Request(Order order, RequestType type, String query) {
		this.order = order;
		this.type = type;
		this.query = query;
	}
	
	public String getQuery() {
		return query;
	}
	public Order getOrder() {
		return order;
	}
	public RequestType getType() {
		return type;
	}
}
