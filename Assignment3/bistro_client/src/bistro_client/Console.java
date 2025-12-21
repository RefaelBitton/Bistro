package bistro_client;


import java.io.IOException;

import boundry.IController;
import entities.Request;
import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Console {
    BistroClient bc;
    final public static int DEFAULT = 5556;

    public Console(String host, int port) {
        bc = new BistroClient(host,port);
    }
    
  	public void setController(IController controller) {
    	  bc.setController(controller);
  	}
  	
    public void accept(Request r) //sending request to server
    {
    	try {
			bc.sendToServer(r);
		} catch (IOException e) {
			System.out.println("Error sending request to server!");
			e.printStackTrace();
		}
    }
    
	public void switchScreen(Object controller, ActionEvent event, String newScreenPath, User user) { //switching screens
		try{
//    		FXMLLoader loader = new FXMLLoader();					// Create a new FXMLLoader instance
//	    	((Node)event.getSource()).getScene().getWindow().hide();    // Hide primary window (current window)
//	    	Stage primaryStage = new Stage();							// Create a new stage (new window)							
//	    	Pane root = loader.load(controller.getClass().getResource(newScreenPath).openStream()); // Load the new screen FFXML file
//	    	Scene scene = new Scene(root);								// Create a new scene with the loaded UI
//	    	primaryStage.setScene(scene);								// Set the scene to the new stage
//	    	primaryStage.show();										// Show the new window
        	FXMLLoader loader = new FXMLLoader(getClass().getResource(newScreenPath));
        	Parent root = loader.load();
        	Object main = loader.getController();
        	((IController)main).setUser(user);
        	Scene scene = new Scene(root);
        	Stage primaryStage = new Stage();
        	((Node)event.getSource()).getScene().getWindow().hide();    // Hide primary window (current window)
        	primaryStage.setScene(scene);
        	primaryStage.show();
    	} catch (IOException e) {
    		e.printStackTrace();
    		System.out.println("Couldn't switch to screen " + newScreenPath);
    	}          
	}
	

}