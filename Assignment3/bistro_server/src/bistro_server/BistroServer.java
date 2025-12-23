package bistro_server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import entities.Request;
import ocsf.server.*;

public class BistroServer extends AbstractServer {

    public static final int DEFAULT_PORT = 5556;
    protected static List<ConnectionToClient> clients;
    DBconnector dbcon;

    public BistroServer(int port) {
        super(port);
        dbcon = new DBconnector();
        clients = Collections.synchronizedList(new ArrayList<>());
    }

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

    @Override
    protected void clientConnected(ConnectionToClient client) {
        clients.add(client);
        MainScreenServerController.refreshClientsLive();
    }

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
