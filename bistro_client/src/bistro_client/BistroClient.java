package bistro_client;
import java.io.IOException;

import boundry.OrderScreenController;
import boundry.SearchScreenController;
import ocsf.client.*;

public class BistroClient extends AbstractClient{
    SearchScreenController searchController;
    OrderScreenController orderController;
	public BistroClient(String host, int port) {
        super(host, port);
        System.out.println("on port: "+ port );
        try {
            openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void handleMessageFromServer(Object msg) {
    	String result = (String)msg;    	
    	if(searchController==null) {
    		this.orderController.setResultText(result);
    	}
    	else {
    		this.searchController.setResultTxt(result);
    	}
    	
    }
    
    public void setSearchController(SearchScreenController controller) {
        this.searchController = controller;
    }
    
    public void setOrderController(OrderScreenController controller) {
    	this.orderController = controller;
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