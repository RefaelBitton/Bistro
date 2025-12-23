package boundry;

import entities.User;

/**an interface that is implemented by all controllers */
public interface IController {
	/**
	 * displays the result of a request back to the user
	 * @param result the result of a query to display to the user
	 */
	public void setResultText(String result);
	/**
	 * a method that changes the controller's 'user' field to the one given
	 * @param user the user that is currently using the controller (the session)
	 */
	public void setUser(User user);
}
