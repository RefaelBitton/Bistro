package bistro_client;


import java.io.IOException;

import boundry.SearchScreenController;
import entities.Request;

public class Console {
    BistroClient bc;
    final public static int DEFAULT = 5556;

    public Console(String host, int port) {
        bc = new BistroClient(host,port);
    }
    
    public void setController(SearchScreenController cont) {
    	bc.setController(cont);
    }

    public void accept(Request r) 
    {
    	try {
			bc.sendToServer(r);
		} catch (IOException e) {
			System.out.println("Error sending request to server!");
			e.printStackTrace();
		}
    }
//        try
//        {
//          BufferedReader fromConsole = 
//          new BufferedReader(new InputStreamReader(System.in));
//          String message;
//
//          ArrayList<String> arr = new ArrayList<>();
//
//          while (true) 
//          {
//            message = fromConsole.readLine();
//            if(message.equals("send")) {
//                bc.sendOrderToServer(arr);
//                arr.clear();
//            }
//            else {
//                arr.add(message);
//            }
//          }
//        } 
//        catch (Exception ex) 
//        {
//          System.out.println
//            ("Unexpected error while reading from console!");
//        }
//     }

//    public static void main(String[] args) {
//        String host = "localhost";
//        int port = 5556;
//        Console con = new Console(host,port);
//        con.accept();
//    }
}