package bistro_client;
import java.io.IOException;
import java.util.ArrayList;

import boundry.SearchScreenController;
import javafx.application.Platform;
import ocsf.client.*;

public class BistroClient extends AbstractClient{
    SearchScreenController controller;
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
//        System.out.println("Client: handleMessageFromServer called from "+this+" , msg = " + msg);
    	String result = (String)msg;    	
    	//TODO: change to something normal
    	Platform.runLater(() -> {
            if (this.controller != null) {
                this.controller.setResultTxt(result);
            }
            else{System.out.println("Controller is null!");}
            System.out.println(result);
        });
    }
    
    public void setController(SearchScreenController controller) {
        System.out.println("BistroClient: setController " + controller +" on "+this);
        this.controller = controller;
    }

    public void sendOrderToServer(Object str) {
        ArrayList<String> userInput = (ArrayList<String>)str;
        try {
            sendToServer(userInput);
        } catch (IOException e) {
            e.printStackTrace();
            quit();
        }
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