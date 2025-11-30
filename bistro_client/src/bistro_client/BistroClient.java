package bistro_client;
import java.io.IOException;
import java.util.ArrayList;

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

    public void sendOrderToServer(Object str) {
        ArrayList<String> userInput = (ArrayList<String>)str;
        try {
            sendToServer(userInput);
        } catch (IOException e) {
            e.printStackTrace();
            quit();
        }
    }

    public void quit() {
        try {
            closeConnection();
        } catch (IOException e1) {
            e1.printStackTrace();
            System.exit(0);
        }
    }

}