package bistro_server;
import java.util.ArrayList;
import bistro_client.*;
import entities.Request;
import entities.RequestType;
import ocsf.server.*;

public class BistroServer extends AbstractServer {

     final public static int DEFAULT_PORT = 5556;
     DBconnector dbcon = new DBconnector();

    public BistroServer(int port) {
        super(port);
    }

    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        Request r = (Request)msg;
        if(r.getType()==RequestType.WRITE) {
	        dbcon.handleQuerries(r);
	        System.out.println("Added the following order to the DB:");
	        System.out.println(r.getOrder());
        }
        
    }

    public static void main(String[] args) 
      {
        int port = 0; //Port to listen on

        try
        {
          port = Integer.parseInt(args[0]); //Get port from command line
        }
        catch(Throwable t)
        {
          port = DEFAULT_PORT; //Set port to 5555
        }

        BistroServer sv = new BistroServer(port);

        try 
        {
          sv.listen(); //Start listening for connections
        } 
        catch (Exception ex) 
        {
          System.out.println("ERROR - Could not listen for clients!");
        }
      }

}