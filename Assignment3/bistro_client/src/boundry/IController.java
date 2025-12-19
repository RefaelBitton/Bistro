package boundry;

import entities.User;

//using interface because there are some controllers that using same method(setResultText)
public interface IController {
	public void setResultText(String result);
	public void setUser(User user);
}
