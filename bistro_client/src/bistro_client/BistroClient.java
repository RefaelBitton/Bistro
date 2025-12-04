package bistro_client;
import java.io.IOException;

import boundry.OrderScreenController;
import boundry.SearchScreenController;
import boundry.UpdateScreenController;
import ocsf.client.*;

public class BistroClient extends AbstractClient{
    SearchScreenController searchController;
    OrderScreenController orderController;
    UpdateScreenController updateController;
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
    	if(searchController==null && updateController==null) {
    		this.orderController.setResultText(result);
    	}
    	else if(orderController==null && updateController==null){
    		this.searchController.setResultTxt(result);
    	}
    	else {
    		this.updateController.setResultText(result);
    	}
    	
    }
    
    public void setSearchController(SearchScreenController controller) {
        this.searchController = controller;
    }
    
    public void setOrderController(OrderScreenController controller) {
    	this.orderController = controller;
    }
    
    public void setUpdateController(UpdateScreenController controller) {
    	this.updateController = controller;
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