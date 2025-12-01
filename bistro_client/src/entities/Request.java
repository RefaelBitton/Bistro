package entities;

public abstract class Request {
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
