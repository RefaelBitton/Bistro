package entities;

import java.io.Serializable;
import java.util.List;

public class Worker extends Subscriber implements Serializable {
	private static final long serialVersionUID = 1L;
	private int subscriberID;
	private String userName;
	private String firstName;
	private String lastName;
	private List<Order> orderHistory;
	
	public Worker(int subscriberID, String userName, String firstName, String lastName, String phoneNumber,
			String email, List<Order> orderHistory) {
		super(subscriberID,userName,firstName,lastName,phoneNumber,email,orderHistory);
		setType(UserType.BISTRO_REP);
	}
}
