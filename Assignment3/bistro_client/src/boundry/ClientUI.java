package boundry;

import bistro_client.Console;
import javafx.application.Application;
import javafx.stage.Stage;
/**
 * The entry point for the client's side of the system
 * */
public class ClientUI extends Application {
	/** responsible for handling the sending of requests to the server*/
	public static Console console;
	public static void main( String args[] ) throws Exception
	   { 
			console = new Console("localhost",5556);
		    launch(args);  
	   }
	 
	@Override
	public void start(Stage primaryStage) throws Exception {
		LoginScreenController aFrame = new LoginScreenController();
		 
		aFrame.start(primaryStage);
	}

}
	
	