package entities;

public class UpdateTableCapacityRequest extends Request {
	private static final long serialVersionUID = 1L;
	private RemoveTableRequest removeReq;
	private AddTableRequest addReq;
	public UpdateTableCapacityRequest(int tableId, int newCap) {
		super(RequestType.UPDATE_TABLE_CAPACITY, "");
		removeReq = new RemoveTableRequest(tableId);
		addReq = new AddTableRequest(newCap);
	}
	
	public RemoveTableRequest getRemoveReq() {
		return removeReq;
	}
	
	public AddTableRequest getAddReq() {
		return addReq;
	}

}
