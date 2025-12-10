package bistro_server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import entities.Request;
import ocsf.server.*;

public class BistroServer extends AbstractServer {
     final public static int DEFAULT_PORT = 5556;
     protected static List<ConnectionToClient> clients;
     DBconnector dbcon;

    public BistroServer(int port) {
        super(port);
        dbcon = new DBconnector(); //connecting server to DB
        clients = new ArrayList<>();
    }

    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) { //handling messages from client
        Request r = (Request)msg;
        String result = dbcon.handleQueries(r);
        if(!result.equals("")) {
        	try {
				client.sendToClient(result); //returning result from client
			} catch (IOException e) {
				System.out.println("sending back to client didn't work");
				e.printStackTrace();
			}
        }
    }
    
    @Override
    protected void clientConnected(ConnectionToClient client) {
    	clients.add(client);
    }

    @Override
    protected void clientDisconnected(ConnectionToClient client) {
    	clients.remove(client);
    }
    
    

    public static void runServer(String p) 
      {
        int port = 0; //Port to listen on

        try
        {
          port = Integer.parseInt(p); //Get port from command line
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