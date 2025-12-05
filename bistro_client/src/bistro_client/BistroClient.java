package bistro_client;
import java.io.IOException;

import boundry.IController;

import ocsf.client.*;

public class BistroClient extends AbstractClient{

    private IController controller;
    //creating client and connecting it to server
	public BistroClient(String host, int port) {
        super(host, port);
        try {
            openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void handleMessageFromServer(Object msg) { //handling message from server
    	String result = (String)msg;
    	controller.setResultText(result);
    }
    
    public void setController(IController controller) {
    	this.controller = controller;
    }
    

    public void quit() { //closing connection between client to server
        try {
            closeConnection();
        } catch (IOException e1) {
            e1.printStackTrace();
            System.exit(0);
        }
    }

}