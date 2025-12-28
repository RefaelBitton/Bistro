package bistro_server;

import java.util.Iterator;
import java.util.NoSuchElementException;

import entities.Order;

public class WaitingList implements Iterable<Order>{
    private WaitlistNode head;
    private WaitlistNode tail;
    private int size;

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
    
    /** Adds a existing order to the front of the waitlist (priority) */
    public void enqueueToHead(Order order) {
        WaitlistNode newNode = new WaitlistNode(order);
        if (head == null) {
            head = tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
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
    public boolean cancel(String orderNumber) {
        WaitlistNode current = head;
        while (current != null) {
            if (current.getOrder().getOrderNumber().equals(orderNumber)) {
                removeNode(current);
                return true;
            }
            current = current.next;
        }
        return false;
    }

    private void removeNode(WaitlistNode node) {
        if (node.prev != null) node.prev.next = node.next;
        else head = node.next;

        if (node.next != null) node.next.prev = node.prev;
        else tail = node.prev;

        size--;
    }
    
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

    public int getSize() { return size; }
    public WaitlistNode getHead() { return head; }
}