package entities;

public class AddTableRequest extends Request {
	private static final long serialVersionUID = 1L;
	int tableCapacity;
	public AddTableRequest(int tableCapacity) {
		super(RequestType.ADD_TABLE, "INSERT INTO `table` (table_number, number_of_seats, active_from, active_to)\n"
				+ "VALUES (?, ?, CURRENT_DATE + INTERVAL 1 MONTH, NULL);");
		this.tableCapacity = tableCapacity;
	}
	
	public int getCap() {
		return tableCapacity;
	}

}
