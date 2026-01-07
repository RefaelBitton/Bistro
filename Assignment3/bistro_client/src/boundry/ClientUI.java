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
	
	/**
	 * The main method to start the client application
	 * 
	 * @param args command line arguments
	 * @throws Exception if an error occurs during startup
	 */
	public static void main( String args[] ) throws Exception
	   { 
			console = new Console("localhost",5556);
		    launch(args);  
	   }

	/**
	 * Starts the JavaFX application and initializes the login screen.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		LoginScreenController aFrame = new LoginScreenController();
		 
		aFrame.start(primaryStage);
	}

}
	
	