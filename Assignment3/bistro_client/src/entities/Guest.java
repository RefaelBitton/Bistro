package entities;

public class Guest extends User {

	public Guest(String email, String phone) {
		super(UserType.GUEST, email, phone);
	}

}
