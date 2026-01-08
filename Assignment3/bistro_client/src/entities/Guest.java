package entities;
/** A type of user describing a guest (not registered in the system)*/
public class Guest extends User {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a Guest user with the specified email and phone.
	 * 
	 * @param email The email of the guest.
	 * @param phone The phone number of the guest.
	 */
	public Guest(String email, String phone,String status) {
		super(UserType.GUEST, email, phone,status);
	}

}
