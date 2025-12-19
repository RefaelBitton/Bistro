package entities;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Subscriber extends User implements Serializable {
	private static final long serialVersionUID = 1L;
	private int subscriberID;
	private String userName;
	private String firstName;
	private String lastName;
	private List<Order> orderHistory;
	
	public Subscriber(int subscriberID, String userName, String firstName, String lastName, String phoneNumber,
			String email, List<Order> orderHistory) {
		super(UserType.SUBSCRIBER,email,phoneNumber);
		this.subscriberID = subscriberID;
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.orderHistory = orderHistory;
	}

	public int getSubscriberID() {
		return subscriberID;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public List<Order> getOrderHistory() {
		return orderHistory;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Subscriber))
			return false;
		Subscriber other = (Subscriber) obj;
				return Objects.equals(subscriberID, other.subscriberID)
				&& Objects.equals(userName, other.userName);
	}
		@Override
		public String toString() {
			return "ID: " + subscriberID + "\nUser Name: " + userName + "\nName: " + firstName
					+ " " + lastName + "\nPhone Number: " + getPhone() + "\nEmail: " + getEmail();
		}

	

}

