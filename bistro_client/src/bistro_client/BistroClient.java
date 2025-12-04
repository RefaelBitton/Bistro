package bistro_client;
import java.io.IOException;

import boundry.IController;

import ocsf.client.*;

public class BistroClient extends AbstractClient{

    private IController controller;
	public BistroClient(String host, int port) {
        super(host, port);
        try {
            openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void handleMessageFromServer(Object msg) {
    	String result = (String)msg;
    	controller.setResultText(result);
    }
    
    public void setController(IController controller) {
    	this.controller = controller;
    }
    

    public void quit() {
        try {
            closeConnection();
        } catch (IOException e1) {
            e1.printStackTrace();
            System.exit(0);
        }
    }

}