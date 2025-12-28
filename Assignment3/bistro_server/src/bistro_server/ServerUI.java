package bistro_server;

import javafx.application.Application;
import javafx.stage.Stage;
/**
 * The entry point for the server side of the app
 */
public class ServerUI extends Application {
	private  static MainScreenServerController aFrame;
	
	final public static String DEFAULT_PORT = "5556";
	public static void main( String args[] ) throws Exception
	   { 	
			BistroServer.runServer(DEFAULT_PORT);
		    launch(args);  
	   }
	
	public void start(Stage primaryStage) throws Exception {
		aFrame = new MainScreenServerController();
		 
		aFrame.start(primaryStage);
	}
	public static void updateInScreen(String msg) {
		aFrame.updateTxt(msg);
	}
}
