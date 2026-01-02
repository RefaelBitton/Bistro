package bistro_server;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entities.GetTableRequest;
import entities.Order;
import entities.Table;

public class BistroMonitor implements Runnable {
	private BistroServer server;

	public BistroMonitor(BistroServer server) {
		this.server = server;
	}

	@Override
	public void run() {
		while (true) {
			try {
				checkOrdersAndAdvanceTime();
				Thread.sleep(6000); // Check every 60 seconds
				 BistroServer.dateTime = BistroServer.dateTime.plusMinutes(15);
				 System.out.println("Time advanced to: " + BistroServer.dateTime);

			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			}
		}
	}
	private void checkOrdersAndAdvanceTime() {
	    Map<Table, Order> currentBistro = server.getCurrentBistro();

	    System.out.println("Current simulated time: " + BistroServer.dateTime);

	    for (Map.Entry<Table, Order> entry : currentBistro.entrySet()) {

	        Table table = entry.getKey();
	        Order order = entry.getValue();

	        if (order == null || order.getSittingtime() == null) {
	            continue; // table is empty
	        }

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
		WaitingList RegularList= server.getRegularWaitlist();
		WaitingList InAdvanceList= server.getAdvanceWaitlist();
		Map<Table, Order> currentBistro = server.getCurrentBistro();
		while(currentBistro.containsValue(null)) {
		    	for(Order wl: InAdvanceList) {
		    		//server.reserveTableForOrder(new GetTableRequest(wl.getConfirmationCode(),false));
		    		
			
			
				
			}
		}
	}
}

		
	


	
