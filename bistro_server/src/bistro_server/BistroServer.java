package bistro_server;

import java.io.IOException;

import entities.Request;
import entities.RequestType;
import ocsf.server.*;

public class BistroServer extends AbstractServer {
     final public static int DEFAULT_PORT = 5556;
     DBconnector dbcon;

    public BistroServer(int port) {
        super(port);
        dbcon = new DBconnector();
    }

    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        Request r = (Request)msg;
        String result = dbcon.handleQueries(r);
        if(result != "") {
        	try {
				client.sendToClient(result);
			} catch (IOException e) {
				System.out.println("sending back to client didn't work");
				e.printStackTrace();
			}
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
          port = DEFAULT_PORT; //Set port to 5556
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