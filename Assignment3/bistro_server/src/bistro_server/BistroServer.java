package bistro_server;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import entities.GetTableRequest;
import entities.JoinWaitlistRequest;
import entities.LeaveWaitlistRequest;
import entities.Order;
import entities.Request;
import entities.RequestHandler;
import entities.RequestType;
import entities.ReserveRequest;
import entities.ShowTakenSlotsRequest;
import entities.Table;
import entities.TrySeatRequest;
import entities.WriteRequest;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;
/**The server, extending the abstract server*/
public class BistroServer extends AbstractServer {
     final public static int DEFAULT_PORT = 5556;
     protected static WaitingList waitlistJustArrived = new WaitingList();
     protected static WaitingList waitlistOrderedInAdvance = new WaitingList();

     /**An array that holds the currently connected clients*/
     protected static List<ConnectionToClient> clients;
     /**An array that holds the tables in the restaurant*/
     private static List<Table> tables;
     /**A map that holds the request handlers for each request type*/
    private HashMap<RequestType,RequestHandler> handlers;
    

    private HashMap<Table,Order> currentBistro;
    public static LocalDateTime dateTime = LocalDateTime.of(LocalDate.of(2026, 1, 1), LocalTime.of(15, 00));
    
    private static final DateTimeFormatter DT_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
     /**A connection to the database*/
     DBconnector dbcon;
     
    /**
     * 
     * @param port the port to connect to
     */
    public BistroServer(int port) {
        super(port);
        currentBistro = new HashMap<>();
        dbcon = new DBconnector();
        clients = Collections.synchronizedList(new ArrayList<>());
        tables = dbcon.getAllTables();
        for (Table t : tables) {
			currentBistro.put(t, null);
		}
        tables.sort(null);
        handlers = new HashMap<>();
        currentBistro=new HashMap<>();
        for(Table t:tables) {
			currentBistro.put(t, null);
		}
        handlers.put(RequestType.WRITE_ORDER, this::addNewOrder);
        handlers.put(RequestType.READ_ORDER, dbcon::getOrder);
        handlers.put(RequestType.LOGIN_REQUEST, dbcon::checkLogin);
        handlers.put(RequestType.REGISTER_REQUEST, dbcon::addNewUser);
        handlers.put(RequestType.CANCEL_REQUEST, dbcon::cancelOrder);
        //handlers.put(RequestType.GET_TAKEN_SLOTS, this::checkAvailability);
        handlers.put(RequestType.RESERVE_TABLE, this::reserveTableInAdvance);
        handlers.put(RequestType.JOIN_WAITLIST, this::handleJoinWaitlist);
        handlers.put(RequestType.LEAVE_WAITLIST, this::handleLeaveWaitlist);
        handlers.put(RequestType.UPDATE_DETAILS, dbcon::updateDetails);
        handlers.put(RequestType.ORDER_HISTORY,dbcon::getOrderHistory);
        handlers.put(RequestType.CHECK_CONFCODE, dbcon::checkConfCode);
        handlers.put(RequestType.GET_ALL_ACTIVE_ORDERS, dbcon::getAllActiveOrders);
        handlers.put(RequestType.GET_ALL_SUBSCRIBERS, dbcon::getAllSubscribers);
        //handlers.put(RequestType.TRY_SEAT,this::trySeat);
        handlers.put(RequestType.GET_TABLE, this::getTableForOrder);
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
    
    
    /**
     * Removing a client from the array
     */
//    @Override
//    protected void clientException(ConnectionToClient client, Throwable exception) {
//        clients.remove(client);
//        MainScreenServerController.refreshClientsLive();
//    }

    @Override
    protected void clientException(ConnectionToClient client, Throwable exception) {
        exception.printStackTrace();
    }

    /**Checking if there are available tables for the given order
	 * @param o the order to check availability for
	 * @return whether there are available tables for the order
	 * */
 	
    public synchronized int checkAvailability(List<Table> tables, List<Integer> guests_in_time) {
		Table resultTable = null;
		if (guests_in_time.size()>tables.size()) {
			return -1;
		}
		for(int i = 0; i<guests_in_time.size(); i++) {
			boolean seated = false;
			for (Table t : tables) {
				if (!t.isTaken() && t.getCapacity()>=guests_in_time.get(i)) {
					t.setTaken(true);
					if (i==guests_in_time.size()-1) {
						resultTable = t;
					}
					seated = true;
					break;
				}
			}
			if (!seated) {
				//reset tables
//				for (Table t : tables) {
//					t.setTaken(false);
//				}
				return -1;
			}
		}
		//reset tables
//		for (Table t : tables) {
//			t.setTaken(false);
//		}
    	return resultTable.getId();
	   
    }
    
    /** * Handles a customer leaving the waitlist
	 * Searches by order number
	 * @param r the LeaveWaitlistRequest containing the order number
	 */
    public String handleLeaveWaitlist(Request r) {
        String orderNum = ((LeaveWaitlistRequest)r).getOrderNum();
        // Accessing the static waitlist instance in BistroServer to remove the node
        boolean removed = BistroServer.waitlistJustArrived.cancel(orderNum); 
        
        if (removed) {
            return "You have been removed from the waiting list.";
        } else {
            return "Could not find a waitlist entry with that number.";
        }
    }
    
    
    /** * Handles a walk-in joining the waitlist at the terminal.
     * @param r the JoinWaitlistRequest containing the order details
     */
    
    public String handleJoinWaitlist(Request r) {
        JoinWaitlistRequest req = (JoinWaitlistRequest) r;
        int guests = Integer.parseInt(req.getNumberOfGuests());
        ShowTakenSlotsRequest slotReq = new ShowTakenSlotsRequest(guests, req.getOrderDateTime());
        List<Integer> guestList = prepareGuestsInTimeList(slotReq);
        List<Table> tablesCopy = sortTables(currentBistro.keySet());
        // 1. Check for immediate seating using existing logic
        Order waitlistOrder;
        int tableId = checkAvailability(tablesCopy, guestList);
        System.out.println("Checked availability for walk-in: tableId = " + tableId);
        Table desiredTable = null;
        
        if (tableId != -1) { 
        	waitlistOrder = addNewOrder(new WriteRequest(
        			req.getOrderDateTime(),
        			req.getNumberOfGuests(),
        			req.getSubscriberId(),
        			req.getContact()
        		));
        	for (Table t : tablesCopy) {
        		if (t.getId() == tableId) {
					desiredTable = t;
					break;
				}
        	}
        	waitlistOrder.setSittingtime(BistroServer.dateTime);
        	currentBistro.put(desiredTable, waitlistOrder); // Seat at the first available table

            return "SUCCESS: Table is ready! Please proceed to your table. "
            		+ "Your confirmation code: " + (waitlistOrder.getConfirmationCode());
        } 

        // 2. If no seats and user hasn't confirmed via popup yet
        if (!req.isWaitlistEntry()) {
            return "PROMPT: NO_SEATS_FOUND";
        }

        
        waitlistOrder = addNewOrder(new WriteRequest(
			req.getOrderDateTime(),
			req.getNumberOfGuests(),
			req.getSubscriberId(),
			req.getContact()
		));
        String orderNum = waitlistOrder.getOrderNumber();
        // Add to the DLL queue
        BistroServer.waitlistJustArrived.enqueue(waitlistOrder); 
        
        return "The restaurant is full. You've been added to the waitlist.\n" +
               "Order Number: " + orderNum + "\n" +
               "Confirmation Code: " + waitlistOrder.getConfirmationCode();
    }
    
    // will be called when a table is freed up
    public Order seatFromWaitlist(WaitingList waitlist) {
        // 1. Standard for-each loop made possible by implementing Iterable
        for (Order currentOrder : waitlist) {
            int guests = Integer.parseInt(currentOrder.getNumberOfGuests());
            
            // 2. Check if this specific order fits current availability
            ShowTakenSlotsRequest slotReq = new ShowTakenSlotsRequest(guests, currentOrder.getOrderDateTime());
            List<Integer> guestList = prepareGuestsInTimeList(slotReq);
            List<Table> tablesCopy = sortTables(currentBistro.keySet());
            
            if (checkAvailability(tablesCopy, guestList) != -1) {
                // 3. Remove this specific order from the list and return it
                waitlist.cancel(currentOrder.getOrderNumber());
                return currentOrder;
            }
            // If not a fit, the iterator automatically moves to the next node
        }
        return null; // No one currently in the waitlist fits the free spot
    }
    
    public List<Table> sortTables(Set <Table> tableSet) {
		List<Table> tableList = new ArrayList<>();
		for (Table t : tableSet) {
			System.out.println("Adding table with ID: " + t.getId() + " and capacity: " + t.getCapacity());
			tableList.add(t);
		}
		tableList.sort(null);
		return tableList;
	}
    
    public Order addNewOrder(Request r) {
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
    
    	System.out.println(	dbcon.addOrder(o,req.getQuery()));
    	return o;
    }
    
    // this function will be called when a table is freed up
    
    
    public synchronized String reserveTableInAdvance(Request r) {
    	ReserveRequest req = (ReserveRequest) r;
    	LocalDateTime requested =LocalDateTime.parse(req.getOrderDateTime(), DT_FMT);
		LocalDate requestedDate = requested.toLocalDate();
        LocalDateTime open = LocalDateTime.of(requestedDate, LocalTime.of(11,0));
        LocalDateTime last = LocalDateTime.of(requestedDate, LocalTime.of(21,30));
        LocalDateTime before = requested.minusHours(1).minusMinutes(30);
        LocalDateTime after   = requested.plusHours(1).plusMinutes(30);
        if (before.isBefore(open)) before = open;
        if (after.isAfter(last)) after = last;
    	ShowTakenSlotsRequest slotReq = new ShowTakenSlotsRequest(
				Integer.parseInt(req.getNumberOfGuests()), req.getOrderDateTime(),before,after
				);
    	List<Integer> guests_in_time = prepareGuestsInTimeList(slotReq);
    	//prepare tables copy
    	int available = checkAvailability(tables, guests_in_time);
    	for (Table t : tables) {
    		t.setTaken(false);
    	}
		if (available != -1) {
			addNewOrder(req);
			return "Reservation confirmed.";
		}
		else{
             StringBuilder sb = new StringBuilder("No available tables at requested time. Available slots:\n");
             boolean thereAreOptions = false;
             while (!before.isAfter(after)) {
            	 slotReq = new ShowTakenSlotsRequest(
						 Integer.parseInt(req.getNumberOfGuests()),
						 req.getOrderDateTime(),
						 before,
						 after
						 );
            	 guests_in_time = prepareGuestsInTimeList(slotReq);
            	 if (checkAvailability(tables, guests_in_time) != -1) {
            		 thereAreOptions = true;
            		 sb.append(before.format(DT_FMT).toString()).append("\n");
            	 }
            	 for (Table t : tables) {
            		 t.setTaken(false);
            	 }
            	 before = before.plusMinutes(30);
             }
             return (thereAreOptions)? sb.toString() : "No available tables at requested time or near it.";
             
		}
    }
    
    
    private List<Integer> prepareGuestsInTimeList(Request r ) {
    	ShowTakenSlotsRequest slotReq = (ShowTakenSlotsRequest) r;
    	String open_orders_in_time_string = dbcon.getTakenSlots(slotReq);
    	System.out.println("Open orders in time string: " + open_orders_in_time_string);
		String[] open_orders_in_time_array = open_orders_in_time_string.split(",");
		ArrayList<Integer> guests_in_time = new ArrayList<>();
		//prepare guests in time list
		for (String s : open_orders_in_time_array) {
			if (!s.isEmpty())
				guests_in_time.add(Integer.parseInt(s));
		}
    	guests_in_time.add(slotReq.getNumberOfGuests());
    	return guests_in_time;
		
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
    public String getTableForOrder(Request r) {
		GetTableRequest req = (GetTableRequest) r;
		for (Entry<Table, Order> entry : currentBistro.entrySet()) {
			Order order = entry.getValue();
			if (order != null && order.getConfirmationCode().equals(req.getConfcode())) {
				return entry.getKey().toString();
			}
		}
		//waitlistJustArrived, waitListOrderedInAdvance
		for( Order o : waitlistOrderedInAdvance) {
			if (o.getConfirmationCode().equals(req.getConfcode())) {
				return "Your table will be available soon.";
			}
		}
		for( Order o : waitlistJustArrived) {
			if (o.getConfirmationCode().equals(req.getConfcode())) {
				return "You are currently on the waitlist.";
			}
		}
		String[] args = dbcon.getOrderFromConfCode(req.getQuery(), req.getConfcode()).split(",");
		if (args[0].equals("Not found")) {
			return "No order found with that number";
		}
		String date = args[1];
		int number_of_guests = Integer.parseInt(args[2]);
		
		LocalDateTime orderDate = LocalDateTime.parse(date, DT_FMT);
//		if(orderDate.isBefore(LocalDateTime.now().minusMinutes(15)) || orderDate.isAfter(LocalDateTime.now().plusMinutes(15))) {
//			return "Your reservation is for " + orderDate.format(DT_FMT).toString()+" Please arrive within 15 minutes of your reservation time.";
//		}
		if(orderDate.isBefore(BistroServer.dateTime.minusMinutes(15)) || orderDate.isAfter(BistroServer.dateTime.plusMinutes(15))) {
			return "Your reservation is for " + orderDate.format(DT_FMT).toString()+" Please arrive within 15 minutes of your reservation time.";
		}
		List<Integer> guests_in_time = prepareGuestsInTimeList(new ShowTakenSlotsRequest(number_of_guests,date));
		List<Table> tablesCopy = sortTables(currentBistro.keySet());
		int tableId = checkAvailability(tablesCopy, guests_in_time);
		Order o = new Order(Arrays.asList(args),1);
		if (tableId != -1) {
        	for (Table t : tablesCopy) {
        		System.out.println("Checking table with ID: " + t.getId());
        		if (t.getId() == tableId) {
        			System.out.println("Found desired table with ID: " + t.getId());
        			o.setSittingtime(BistroServer.dateTime);
					currentBistro.put(t, o); // Seat at the first available table
					break;
				}
        	}
			return "Your table is ready! Please proceed to table number "+tableId+".";
		}
		else {
			waitlistOrderedInAdvance.enqueue(o);
			return "No available tables at the moment. Please wait to be seated.";
		}
		
		
		
		
	}
}
