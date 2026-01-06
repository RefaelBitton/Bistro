package bistro_server;

import java.time.LocalDateTime;

import entities.Order;

public class SortedWaitingList extends WaitingList {
	
	@Override
	public void enqueue(Order o) {
	    WaitlistNode newNode = new WaitlistNode(o);

	    LocalDateTime newDt = LocalDateTime.parse(o.getOrderDateTime(),BistroServer.DT_FMT);

	    if (head == null) {
	        head = newNode;
	        size++;
	        return;
	    }

	    WaitlistNode cur = head;

	    // Find first node with datetime >= newDt (insert before it)
	    while (cur != null) {
	        LocalDateTime curDt = LocalDateTime.parse(cur.getOrder().getOrderDateTime());

	        if (!curDt.isBefore(newDt)) {
	            newNode.next = cur;
	            newNode.prev = cur.prev;

	            if (cur.prev != null) {
	                cur.prev.next = newNode;
	            } else {
	                head = newNode;
	            }
	            cur.prev = newNode;

	            size++;
	            return;
	        }

	        if (cur.next == null) break;
	        cur = cur.next;
	    }

	    cur.next = newNode;
	    newNode.prev = cur;
	    size++;
	}
}
