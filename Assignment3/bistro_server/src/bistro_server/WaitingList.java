package bistro_server;

import java.util.Iterator;
import java.util.NoSuchElementException;

import entities.Order;

/** Doubly Linked List implementation for managing the waiting list of customers [cite: 37, 38] */
public class WaitingList implements Iterable<Order>{
    protected WaitlistNode head;
    protected WaitlistNode tail;
    protected int size;

    /** Initializes an empty waiting list */
    public WaitingList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    /** Adds a new customer to the end of the waitlist [cite: 37, 38] */
    public void enqueue(Order order) {
        WaitlistNode newNode = new WaitlistNode(order);
        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }


    /** Removes the person at the front when a table is free  */
    public Order dequeue(WaitlistNode node) {
    	WaitlistNode current = head;
        while (current != null) {
            if (current.equals(node)) {
                removeNode(current);
                return current.getOrder();
            }
            current = current.next;
        }
        return null;
    }

    /** * Handles customer cancellation from anywhere in the queue 
     * Searches by order number
     */
    public String cancel(String ConfirmationCode) {
        WaitlistNode current = head;
        while (current != null) {
            if (current.getOrder().getConfirmationCode().equals(ConfirmationCode)) {
                removeNode(current);
                return current.getOrder().getOrderNumber();
            }
            current = current.next;
        }
        return "not found";
    }

    /** Removes a specific node from the Doubly Linked List
     * @param node The node to be removed 
     *  */
    private void removeNode(WaitlistNode node) {
        if (node.prev != null) node.prev.next = node.next;
        else head = node.next;

        if (node.next != null) node.next.prev = node.prev;
        else tail = node.prev;

        size--;
    }

    /**
     * Returns an iterator to traverse the waiting list
     * @return An iterator for the waiting list
     */
    @Override
    public Iterator<Order> iterator() {
        return new Iterator<Order>() {
            private WaitlistNode current = head;

            @Override
            public boolean hasNext() {
                // Returns true as long as there is another node to visit
                return current != null;
            }

            @Override
            public Order next() {
                if (current == null) {
                    throw new NoSuchElementException();
                }
                // Extract the order and move to the next node in the Doubly Linked List
                Order order = current.getOrder();
                current = current.next;
                return order;
            }
        };
    }
 
	/**
	 * Gets the position of a customer in the queue based on their confirmation code
	 * 
	 * @param ConfirmationCode The confirmation code of the customer
	 * @return The position in the queue (1-based index), or -1 if not found
	 */
    public int getSpotInQueue(String ConfirmationCode) {
		WaitlistNode current = head;
		int position = 1;
		while (current != null) {
			if (current.getOrder().getConfirmationCode().equals(ConfirmationCode)) {
				return position;
			}
			current = current.next;
			position++;
		}
		return -1; // Not found
	}
    
	/**
	 * Gets the current size of the waiting list
	 * 
	 * @return The number of customers in the waiting list
	 */
    public int getSize() { return size; }
    
	/**
	 * Gets the head node of the waiting list
	 * 
	 * @return The head node of the waiting list
	 */
    public WaitlistNode getHead() { return head; }

    /**
     * Returns a string representation of the waiting list
     * @return A string representing the waiting list
     */
    @Override
    public String toString() {
		StringBuilder sb = new StringBuilder("WaitingList{");
		WaitlistNode current = head;
		while (current != null) {
			sb.append(current.getOrder().toString());
			if (current.next != null) sb.append(" <-> ");
			current = current.next;
		}
		sb.append('}');
		return sb.toString();
	}

}