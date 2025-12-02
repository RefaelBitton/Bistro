package bistro_client;
import java.io.IOException;
import boundry.SearchScreenController;
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
    	String result = (String)msg;    	
    	if(controller==null) {
    		System.out.println("Controller is null!");
    	}
    	else {
    		this.controller.setResultTxt(result);
    	}
    	
    }
    
    public void setController(SearchScreenController controller) {
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