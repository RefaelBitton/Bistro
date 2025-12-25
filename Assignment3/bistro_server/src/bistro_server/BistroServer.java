package bistro_server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import entities.Order;
import entities.Request;
import entities.User;
import ocsf.server.*;
/**The server, extending the abstract server*/
public class BistroServer extends AbstractServer {
     final public static int DEFAULT_PORT = 5556;
     protected static WaitingList waitlist = new WaitingList();
     /**An array that holds the currently connected clients*/
     protected static List<ConnectionToClient> clients;

     private static Random rand;

     /**A connection to the database*/
     DBconnector dbcon;
    /**
     * 
     * @param port the port to connect to
     */
    public BistroServer(int port) {
        super(port);
        dbcon = new DBconnector();
        rand = new Random(1_000_000_000);
        clients = Collections.synchronizedList(new ArrayList<>());
    }
    /**
     * Sending messages from client over to the database connector
     */
    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        Request r = (Request) msg;
        System.out.println("In handleMessage before handlequeries");
        String result = dbcon.handleQueries(r);
        System.out.println("In handleMessage after handlequeries");
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
    
    public static int getConfCode() {
    	return rand.nextInt();
    }

//    @Override
//    protected void clientException(ConnectionToClient client, Throwable exception) {
//        clients.remove(client);
//        MainScreenServerController.refreshClientsLive();
//    }
    
    public void enterWaitingList(Order order) {
    	waitlist.enqueue(order);
    }
    
    public Order seatNextInWaitlist() {
		return waitlist.dequeue();
	}
    
    public boolean cancelFromWaitlist(String orderNumber) {
		return waitlist.cancel(orderNumber);
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
