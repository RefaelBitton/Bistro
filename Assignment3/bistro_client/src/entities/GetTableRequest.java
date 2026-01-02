package entities;

public class GetTableRequest extends Request {
	private static final long serialVersionUID = 1L;
	private String confcode;
	private boolean isForNow;
	public GetTableRequest(String confcode,boolean isForNow) {
		super(RequestType.GET_TABLE,"SELECT * FROM `order` WHERE confirmation_code = ?;");
		this.confcode=confcode;
		this.isForNow=isForNow;
	}
	public String getConfcode() {
		return confcode;
	}
	
	public boolean isForNow() {
		return isForNow;
	}

}
