package bistro_client;
import java.io.IOException;

import boundry.IController;
import javafx.application.Platform;
import ocsf.client.*;
/**
 * The client itself, extending abstract client and responsible for sending and recieving messages from the server
 * */
public class BistroClient extends AbstractClient{
	/**The controller of the screen currently being displayed*/
    private IController controller;
    /**
     * creating client and connecting it to server
     * @param host
     * @param port
     */
	public BistroClient(String host, int port) {
        super(host, port);
        try {
            openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	/**
	 * handling message from server
	 */
	@Override
	protected void handleMessageFromServer(Object msg) {
	    controller.setResultText(msg);
	}

    /**Setting the controller field whenever a screen is switched*/
    public void setController(IController controller) {
    	this.controller = controller;
    }
    
    /**
     * closing connection between client to server
     */
    public void quit() {
        try {
            closeConnection();
        } catch (IOException e1) {
            e1.printStackTrace();
            System.exit(0);
        }
    }

}