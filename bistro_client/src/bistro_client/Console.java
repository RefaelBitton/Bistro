package bistro_client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Console {
    BistroClient bc;
    final public static int DEFAULT = 5556;

    public Console(String host, int port) {
        bc = new BistroClient(host,port);
    }

    public void accept() 
    {
        try
        {
          BufferedReader fromConsole = 
          new BufferedReader(new InputStreamReader(System.in));
          String message;

          ArrayList<String> arr = new ArrayList<>();

          while (true) 
          {
            message = fromConsole.readLine();
            if(message.equals("send")) {
                bc.sendOrderToServer(arr);
                arr.clear();
            }
            else {
                arr.add(message);
            }
          }
        } 
        catch (Exception ex) 
        {
          System.out.println
            ("Unexpected error while reading from console!");
        }
     }

    public static void main(String[] args) {
        String host = "localhost";
        int port = 5556;
        Console con = new Console(host,port);
        con.accept();
    }
}