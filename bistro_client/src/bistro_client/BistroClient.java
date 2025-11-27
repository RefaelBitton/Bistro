package bistro_client;
import java.io.IOException;

import ocsf.client.*;

public class BistroClient extends AbstractClient{

    public BistroClient(String host, int port) {
        super(host, port);
        try {
            openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void handleMessageFromServer(Object msg) {

    }

    public void sendOrderToServer() {
        String str = "testing";
        try {
            sendToServer(str);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                closeConnection();
            } catch (IOException e1) {
                e1.printStackTrace();
                System.exit(0);
            }
        }
    }

    public static void main(String[] args) {
        String host = "localhost";
        int port = 5556;
        BistroClient client = new BistroClient(host,port);
        client.sendOrderToServer();
    }

}