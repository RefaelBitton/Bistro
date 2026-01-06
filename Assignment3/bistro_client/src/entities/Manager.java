package entities;

import java.io.Serializable;
import java.util.List;

public class Manager extends Worker implements Serializable {
	private static final long serialVersionUID = 1L;
	private int subscriberID;
	private String userName;
	private String firstName;
	private String lastName;
	private List<Order> orderHistory;
	
	public Manager(int subscriberID, String userName, String firstName, String lastName, String phoneNumber,
			String email,String status, List<Order> orderHistory) {
		super(subscriberID,userName,firstName,lastName,phoneNumber,email,status,orderHistory);
		setType(UserType.MANAGER);
	}
}
