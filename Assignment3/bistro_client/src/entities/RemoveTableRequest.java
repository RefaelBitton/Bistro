package entities;

public class RemoveTableRequest extends Request {
	private static final long serialVersionUID = 1L;
	int tableID;
	public RemoveTableRequest(int tableID) {
		super(RequestType.REMOVE_TABLE, "UPDATE `table` SET active_to = CURRENT_DATE + INTERVAL 1 MONTH WHERE table_number = ?;");
		this.tableID = tableID;
	}
	public int getId() {
		return tableID;
	}

}
