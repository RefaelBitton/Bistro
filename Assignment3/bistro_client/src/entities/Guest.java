package entities;
//** A type of user describing a guest (not registered in the system)*/
public class Guest extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Guest(String email, String phone,String status) {
		super(UserType.GUEST, email, phone,status);
	}

}
