package boundry;


import bistro_client.Console;
import javafx.application.Application;
import javafx.stage.Stage;

public class ClientUI extends Application {
	public Console con; //only one instance

	public static void main( String args[] ) throws Exception
	   { 
		    launch(args);  
	   } // end main
	 
	@Override
	public void start(Stage primaryStage) throws Exception {
		 con= new Console("localhost", 5555);
		// TODO Auto-generated method stub
						  		
		MainScreenController aFrame = new MainScreenController(); // create StudentFrame
		 
		aFrame.start(primaryStage);
	}
}
	
	