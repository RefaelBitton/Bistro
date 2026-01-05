package entities;

public class GetTableRequest extends Request {
	private static final long serialVersionUID = 1L;
	private String confcode;
	public GetTableRequest(String confcode) {
		super(RequestType.GET_TABLE,"SELECT * FROM `order` WHERE confirmation_code = ?;");
		this.confcode=confcode;
	}
	public String getConfcode() {
		return confcode;
	}
}
