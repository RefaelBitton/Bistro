package entities;
//** A type of user describing a guest (not registered in the system)*/
public class Guest extends User {

	public Guest(String email, String phone) {
		super(UserType.GUEST, email, phone);
	}

}
