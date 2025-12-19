package entities;

public abstract class User {
	private UserType type;
	private String email;
	private String phone;
	
	public User(UserType type, String email, String phone) {
		this.type = type;
		this.email = email;
		this.phone = phone;
	}
		
	public UserType getType() {
		return type;
	}
	
	public void setType(UserType type) {
		this.type = type;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}

}
