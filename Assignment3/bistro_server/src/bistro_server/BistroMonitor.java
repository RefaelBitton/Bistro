package bistro_server;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import entities.CancelRequest;
import entities.GetTableRequest;
import entities.Order;
import entities.ShowTakenSlotsRequest;
import entities.Table;

public class BistroMonitor implements Runnable {
	private BistroServer server;
	private Map<Order, LocalDateTime> pending;

	public BistroMonitor(BistroServer server) {
		pending = new HashMap<>();
		this.server = server;
	}

	@Override
	public void run() {
		while (true) {
			try {
				checkOrdersAndAdvanceTime();
				trySeatFromWaitlist();
				checkPendingOrders();
				checkExpiredOrders();
				notifyAboutOrder();
				Thread.sleep(60000); // Check every 60 seconds
				 BistroServer.dateTime = BistroServer.dateTime.plusMinutes(15);
				 System.out.println("Time advanced to: " + BistroServer.dateTime);

			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			}
		}
	}
	
	private void notifyAboutOrder() {
		Map<String,String> contacts=server.dbcon.OrdersToNotify();
		for(Map.Entry<String, String> entry : contacts.entrySet()) {
	    	String orderNumber=entry.getKey();
	    	String contact=entry.getValue();
	    	System.out.println(" Order " + orderNumber
					+ " has been notified thru contact.");
			ServerUI.updateInScreen("for contact: "+ contact+
				"\n your order " + orderNumber
				+ " is in 2 hours" 
			);
	    }
		
	}
	
	/**
	 * A method that checks orders that ordered in advance and didn't arrive yet
	 */
	private void checkExpiredOrders() {
		Map<Table, Order> currentBistro = server.getCurrentBistro();
		Set<String> expiredOrders = new HashSet<>();
		for(Order o:currentBistro.values()) {
			if(o!=null) {
				expiredOrders.add(o.getOrderNumber());
			}
		
		}
	    Map<String,String> contacts=server.dbcon.ExpirePendingOrders(expiredOrders);
	    for(Map.Entry<String, String> entry : contacts.entrySet()) {
	    	String orderNumber=entry.getKey();
	    	String contact=entry.getValue();
	    	System.out.println(" Order " + orderNumber
					+ " has expired before seating.");
			ServerUI.updateInScreen("for contact: "+ contact+
				" Order " + orderNumber
				+ " has expired before seating, please contact the bistro staff."
			);
	    }
	      
	}
	/**
	 * A method that checks orders that entered through the waiting lists and didn't arrive yet 
	 */
	private void checkPendingOrders() {
		List<Order> toRemove = new ArrayList<>();
		Map<Table, Order> currentBistro = server.getCurrentBistro();
	    for (Map.Entry<Order, LocalDateTime> entry : pending.entrySet()) {
	        Order order = entry.getKey();
	        LocalDateTime addedTime = entry.getValue();
	        if (BistroServer.dateTime.isAfter(addedTime.plusMinutes(15))) {
	            // Time exceeded 15 minutes
	            System.out.println(" Seating time exceeded for order: " + order.getConfirmationCode());
	            ServerUI.updateInScreen("for contact: "+ order.getContact()+
	                " \n Seating time exceeded for order: " + order.getConfirmationCode()
	                + " please contact the bistro staff."
	            );
	            server.dbcon.cancelOrder(new CancelRequest(order.getOrderNumber(),order.getConfirmationCode()));
	            for(Map.Entry<Table, Order> tableEntry : currentBistro.entrySet()) {
	            	if(tableEntry.getValue()!=null && tableEntry.getValue().getConfirmationCode().equals(order.getConfirmationCode())) {
	            		Table table=tableEntry.getKey();
	            		table.setTaken(false);
	            		currentBistro.put(table, null);
	            		break;
	            	}
	            }
	            toRemove.add(order);
	        }
	    }
	        
	    for(Order o:toRemove) {
	    	 pending.remove(o);
	    	  
	    }
	}
	
	
	private void checkOrdersAndAdvanceTime() {
	    Map<Table, Order> currentBistro = server.getCurrentBistro();

	    System.out.println("Current simulated time: " + BistroServer.dateTime);

	    for (Map.Entry<Table, Order> entry : currentBistro.entrySet()) {

	        Table table = entry.getKey();
	        Order order = entry.getValue();

	        if (order == null ) {
	            continue; // table is empty
	        }
	        System.out.println("Checking order number " + order.getOrderNumber());

	        LocalDateTime sittingTime = order.getSittingtime();
	        LocalDateTime twoHoursAfterSitting = sittingTime.plusHours(2);

	        //  check if current time is AFTER sitting time + 2 hours
	        if (!BistroServer.dateTime.isBefore(twoHoursAfterSitting)) {

	            
	            System.out.println(" Order " + order.getConfirmationCode()
	                    + " exceeded 2 hours at table " + table.getId());
	            ServerUI.updateInScreen("contact: "+ order.getContact()+
	                "Order " + order.getConfirmationCode()
	                + " exceeded 2 hours at table "  + table.getId() + " please progress to 'leave table' at our terminal or app"
	            );
	        }
	    }

	}
	private void trySeatFromWaitlist() {
		System.out.println("------------------------------------------");
		List<WaitlistNode> toRemove = new ArrayList<>();
		WaitingList RegularList= server.getRegularWaitlist();
		int res;
		WaitingList InAdvanceList= server.getAdvanceWaitlist();
		Map<Table, Order> currentBistro = server.getCurrentBistro();
	    for(Order wl: InAdvanceList) {
	    	if(!currentBistro.containsValue(null)) {
	    		break;
	    	}
		    res=trySeatHelper(wl,InAdvanceList);
		    System.out.println("Tried to seat order from inAdvance, confCode: "+wl.getConfirmationCode()+", result is: "+res);
		    if(res!=-1) {
		    	toRemove.add(new WaitlistNode(wl));
		    	ServerUI.updateInScreen("for contact: "+wl.getContact()+"\n table number  "+res+" got available for you,please come to the Bistro within 15 minute from this massage");
		   	}
	    }
	    for(WaitlistNode node : toRemove) {
	    	InAdvanceList.dequeue(node);
	    }
	    toRemove.clear();
		for(Order wl2: RegularList) {
			if(!currentBistro.containsValue(null)) {
	    		break;
			}
		    res=trySeatHelper(wl2,RegularList);
		    System.out.println("Tried to seat order from regular, confCode: "+wl2.getConfirmationCode()+", result is: "+res);
		    if(res!=-1) {
		    	toRemove.add(new WaitlistNode(wl2));
		    	ServerUI.updateInScreen("for contact: "+wl2.getContact()+"\n table number  "+res+" got available for you,please come to the Bistro within 15 minute from this massage");
		    }
		}
		for(WaitlistNode node : toRemove) {
	    	RegularList.dequeue(node);
	    }
		
	}
	    
			
		
	
	private int trySeatHelper(Order order,WaitingList waitlist) {
		Map<Table, Order> currentBistro = server.getCurrentBistro();
		int guests=Integer.parseInt(order.getNumberOfGuests());
		String confcode=order.getConfirmationCode();
		Map<String,Integer> guests_in_time = server.prepareGuestsInTimeList(new ShowTakenSlotsRequest(guests,confcode), false);
		for (Order o : currentBistro.values()) {
			if (o != null) {
				guests_in_time.remove(o.getConfirmationCode());
			}
		}
		guests_in_time.remove(confcode);
		
		System.out.println("Guests in time list: " + guests_in_time.toString());
		List<Table> tablesCopy = server.sortTables(currentBistro.keySet(),true);
		Map<String,Integer> tempGuestsInTime = new HashMap<>();
		tempGuestsInTime.put(confcode,guests);
		int tableId = server.checkAvailability(tablesCopy, tempGuestsInTime,confcode);
		System.out.println("Tables copy: " + tablesCopy.toString());
		int canSeatOthers = server.checkAvailability(tablesCopy, guests_in_time,confcode);
		System.out.println("Current bistro status: " + currentBistro.toString());
		Table desiredTable = null;
		if (tableId != -1 && canSeatOthers == 0) {
        	for (Table t : currentBistro.keySet()) {
        		System.out.println("Checking table with ID: " + t.getId());
        		if (t.getId() == tableId) {
        			desiredTable = t;
        			System.out.println("Found desired table with ID: " + t.getId());	
					currentBistro.put(t, order); // Seat at the first available table
					t.setTaken(true);
					pending.put(order, BistroServer.dateTime);
					break;
				}
        	}
			return (desiredTable !=null)? desiredTable.getId() : -1;
		}
		return -1;
	}
}