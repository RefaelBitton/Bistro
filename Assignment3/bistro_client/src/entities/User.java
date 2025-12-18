package entities;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	private int subscriberID;
	private String userName;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String email;
	private List<Order> orderHistory;
	public User(int subscriberID, String userName, String firstName, String lastName, String phoneNumber,
			String email, List<Order> orderHistory) {
		this.subscriberID = subscriberID;
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.orderHistory = orderHistory;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
		if (!(obj instanceof User))
			return false;
		User other = (User) obj;
				return Objects.equals(subscriberID, other.subscriberID)
				&& Objects.equals(userName, other.userName);
	}
		@Override
		public String toString() {
			return "ID: " + subscriberID + "\nUser Name: " + userName + "\nName: " + firstName
					+ " " + lastName + "\nPhone Number: " + phoneNumber + "\nEmail: " + email;
		}

	

}

