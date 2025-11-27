package bistro_server;
import ocsf.server.*;

public class BistroServer extends AbstractServer {

     final public static int DEFAULT_PORT = 5556;

    public BistroServer(int port) {
        super(port);
    }

    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        String str = (String)msg;
        System.out.println(str);
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