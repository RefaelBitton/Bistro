package boundry;


import bistro_client.Console;
import javafx.application.Application;
import javafx.stage.Stage;

public class ClientUI extends Application {
	public static Console console = new Console("localhost",5556);
	public static void main( String args[] ) throws Exception
	   { 
		    launch(args);  
	   }
	 
	@Override
	public void start(Stage primaryStage) throws Exception {
		MainScreenController aFrame = new MainScreenController();
		 
		aFrame.start(primaryStage);
	}
}
	
	