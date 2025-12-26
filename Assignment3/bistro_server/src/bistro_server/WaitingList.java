package bistro_server;

import entities.Order;

public class WaitingList {
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

    /** Removes the person at the front when a table is free  */
    public Order dequeue() {
        if (head == null) return null;
        Order order = head.getOrder();
        head = head.next;
        if (head != null) head.prev = null;
        else tail = null;
        size--;
        return order;
    }

    /** * Handles customer cancellation from anywhere in the queue 
     * Searches by order number or confirmation code
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

    public int getSize() { return size; }
}