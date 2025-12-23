package bistro_server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import entities.Request;
import ocsf.server.*;
/**The server, extending the abstract server*/
public class BistroServer extends AbstractServer {
     final public static int DEFAULT_PORT = 5556;
     /**An array that holds the currently connected clients*/
     protected static List<ConnectionToClient> clients;
     /**A connection to the database*/
     DBconnector dbcon;
    /**
     * 
     * @param port the port to connect to
     */
    public BistroServer(int port) {
        super(port);
        dbcon = new DBconnector();
        clients = Collections.synchronizedList(new ArrayList<>());
    }
    /**
     * Sending messages from client over to the database connector
     */
    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        Request r = (Request) msg;
        String result = dbcon.handleQueries(r);

        try {
            client.sendToClient(result); // ALWAYS send response
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**Adding a client to the array*/
    @Override
    protected void clientConnected(ConnectionToClient client) {
        clients.add(client);
        MainScreenServerController.refreshClientsLive();
    }
    /**
     * Removing a client from the array
     */
    @Override
    protected void clientDisconnected(ConnectionToClient client) {
        clients.remove(client);
        MainScreenServerController.refreshClientsLive();
    }

    @Override
    protected void clientException(ConnectionToClient client, Throwable exception) {
        clients.remove(client);
        MainScreenServerController.refreshClientsLive();
    }
    
    
    /**Starting the server
     * @param p the port to listen on
     * */
    public static void runServer(String p) {
        int port;
        try {
            port = Integer.parseInt(p);
        } catch (Exception e) {
            port = DEFAULT_PORT;
        }

        BistroServer sv = new BistroServer(port);
        try {
            sv.listen();
        } catch (Exception ex) {
            System.out.println("ERROR - Could not listen for clients!");
        }
    }
}
