package bistro_server;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import entities.JoinWaitlistRequest;
import entities.LeaveWaitlistRequest;
import entities.Order;
import entities.Request;
import entities.RequestHandler;
import entities.RequestType;
import entities.ReserveRequest;
import entities.ShowTakenSlotsRequest;
import entities.Table;
import entities.WriteRequest;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;
/**The server, extending the abstract server*/
public class BistroServer extends AbstractServer {
     final public static int DEFAULT_PORT = 5556;
     protected static WaitingList waitlist = new WaitingList();
     /**An array that holds the currently connected clients*/
     protected static List<ConnectionToClient> clients;
     /**An array that holds the tables in the restaurant*/
     private static List<Table> tables;
     /**A map that holds the request handlers for each request type*/
    private HashMap<RequestType,RequestHandler> handlers;

    
    private static final DateTimeFormatter DT_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
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
        tables = dbcon.getAllTables();
        tables.sort(null);
        handlers = new HashMap<>();
        handlers.put(RequestType.WRITE_ORDER, this::addNewOrder);
        handlers.put(RequestType.READ_ORDER, dbcon::getOrder);
        handlers.put(RequestType.LOGIN_REQUEST, dbcon::checkLogin);
        handlers.put(RequestType.REGISTER_REQUEST, dbcon::addNewUser);
        handlers.put(RequestType.CANCEL_REQUEST, dbcon::cancelOrder);
        handlers.put(RequestType.GET_TAKEN_SLOTS, this::checkAvailability);
        handlers.put(RequestType.RESERVE_TABLE, this::reserveTable);
        handlers.put(RequestType.JOIN_WAITLIST, this::handleJoinWaitlist);
        handlers.put(RequestType.LEAVE_WAITLIST, this::handleLeaveWaitlist);
        handlers.put(RequestType.UPDATE_DETAILS, dbcon::updateDetails);
        handlers.put(RequestType.ORDER_HISTORY,dbcon::getOrderHistory);
    }
    /**
     * Sending messages from client over to the database connector
     */
    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        Request r = (Request) msg;
        RequestHandler handler = handlers.get(r.getType());
        System.out.println("Handling request of type: " + r.getType());
        Object result = handler.handle(r);
        System.out.println("Request of type " + r.getType() + " handled with result: " + result.toString());
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
    

//    @Override
//    protected void clientException(ConnectionToClient client, Throwable exception) {
//        exception.printStackTrace();
//    }

    /**Checking if there are available tables for the given order
	 * @param o the order to check availability for
	 * @return whether there are available tables for the order
	 * */
    public synchronized Boolean checkAvailability(Request r) {
 	
    	ShowTakenSlotsRequest req = (ShowTakenSlotsRequest) r;
    	ShowTakenSlotsRequest slotReq = new ShowTakenSlotsRequest(req.getNumberOfGuests(), req.getOrderDateTime());
    	String open_orders_in_time_string = dbcon.getTakenSlots(slotReq);
    	System.out.println("Open orders in time string: " + open_orders_in_time_string);
		String[] open_orders_in_time_array = open_orders_in_time_string.split(",");
		ArrayList<Integer> guests_in_time = new ArrayList<>();
		for (String s : open_orders_in_time_array) {
			if (!s.isEmpty())
				guests_in_time.add(Integer.parseInt(s));
		}
		guests_in_time.add(req.getNumberOfGuests());
		if (guests_in_time.size()>tables.size()) {
			return false;
		}
		for(int guests : guests_in_time) {
			boolean seated = false;
			for (Table t : tables) {
				if (!t.isTaken() && t.getCapacity()>=guests) {
					t.setTaken(true);
					seated = true;
					break;
				}
			}
			if (!seated) {
				//reset tables
				for (Table t : tables) {
					t.setTaken(false);
				}
				return false;
			}
		}
		//reset tables
		for (Table t : tables) {
			t.setTaken(false);
			}
    	return true;
	   
    }
    
    private String handleLeaveWaitlist(Request r) {
        String orderNum = ((LeaveWaitlistRequest)r).getOrderNum();
        // Accessing the static waitlist instance in BistroServer to remove the node
        boolean removed = BistroServer.waitlist.cancel(orderNum); 
        
        if (removed) {
            return "✅ You have been removed from the waiting list.";
        } else {
            return "❌ Could not find a waitlist entry with that number.";
        }
    }
    
    /** * Handles a walk-in joining the waitlist at the terminal.
     * Assumptions: checkImmediateAvailability(int guests) is implemented elsewhere.
     */
    private String handleJoinWaitlist(Request r) {
        JoinWaitlistRequest req = (JoinWaitlistRequest) r;
        Order order = req.getOrder();
        int guests = Integer.parseInt(order.getNumberOfGuests());
        ShowTakenSlotsRequest slotReq = new ShowTakenSlotsRequest(guests, order.getOrderDateTime());
        // 1. Check for immediate seating per requirement
        if (checkAvailability(slotReq)) { // dummy method
            // Seat immediately - skip waitlist
            return "✅ Welcome! A table is available right now. Please proceed to your table.";
        } else {
            // 2. No immediate room -> Add to the Doubly Linked List
            
            // Retrieve the confirmation code from the order object
            String confCode = order.getConfirmationCode();
            
            // Use the waitlist instance directly
            BistroServer.waitlist.enqueue(order); 
            
            return "⏳ The restaurant is currently full. You have been added to the waiting list.\n" +
                   "Confirmation Code: " + confCode + "\n" +
                   "We will notify you at " + order.getContact() + " when a table is ready.";
        }
    }
    
    public Boolean addNewOrder(Request r) {
    	WriteRequest req = (WriteRequest) r;
    	System.out.println("Adding new order for subscriber ID: " + req.getSubscriberId());
    	String email;
    	if (!req.getSubscriberId().equals("0")) {
    		email = dbcon.readEmail(req.getSubscriberId());
    	}
    	else {
    		email = req.getContact();
    	}
    	
    	
    	System.out.println("Retrieved email: " + email);
    	String orderNumber = dbcon.OrderNumber();
    	System.out.println("Generated order number: " + orderNumber);
    	ArrayList<String> args = new ArrayList<>();
    	args.add(orderNumber);
    	args.add(req.getOrderDateTime());
    	args.add(req.getNumberOfGuests());
    	args.add(1000 + Integer.parseInt(orderNumber) + ""); //confirmation code
    	args.add(req.getSubscriberId());
    	args.add(email);
    	Order o = new Order(args);
    	dbcon.addOrder(o,req.getQuery());
    	System.out.println("Order added to database.");
    	return true;
    }
    public void enterWaitingList(Order order) {
    	waitlist.enqueue(order);
    }
    
    public Order seatNextInWaitlist() {
		return waitlist.dequeue();
	}
    
    public boolean cancelFromWaitlist(String orderNumber) {
		return waitlist.cancel(orderNumber);
	}
    
    public String reserveTable(Request r) {
    	ReserveRequest req = (ReserveRequest) r;
    	ShowTakenSlotsRequest slotReq = new ShowTakenSlotsRequest(
				Integer.parseInt(req.getNumberOfGuests()), req.getOrderDateTime());
    	Boolean available = checkAvailability(slotReq);
		if (available) {
			System.out.println("Table available, adding new order.");
			addNewOrder(req);
			System.out.println("Order added successfully.");
			return "Reservation confirmed.";
		}
		else{
			 LocalDateTime requested =LocalDateTime.parse(req.getOrderDateTime(), DT_FMT);
			 LocalDate requestedDate = requested.toLocalDate();
             LocalDateTime open = LocalDateTime.of(requestedDate, LocalTime.of(11,0));
             LocalDateTime last = LocalDateTime.of(requestedDate, LocalTime.of(21,30));

             LocalDateTime before = requested.minusHours(1).minusMinutes(30);
             LocalDateTime after   = requested.plusHours(1).plusMinutes(30);

             if (before.isBefore(open)) before = open;
             if (after.isAfter(last)) after = last;
             StringBuilder sb = new StringBuilder("No available tables at requested time. Available slots:\n");
             boolean thereAreOptions = false;
             while (!before.isAfter(after)) {
            	 
            	 ShowTakenSlotsRequest newSlotReq = new ShowTakenSlotsRequest(
            			 Integer.parseInt(req.getNumberOfGuests()), before.format(DT_FMT).toString());
            	 if (checkAvailability(newSlotReq)) {
            		 thereAreOptions = true;
            		 sb.append(before.format(DT_FMT).toString()).append("\n");
            	 }
            	 before = before.plusMinutes(30);
             }
             return (thereAreOptions)? sb.toString() : "No available tables at requested time or near it.";
             
		}
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
