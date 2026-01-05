package bistro_server;

import entities.Order;

/**
 * Represents a node in the Doubly Linked Waitlist
 */
public class WaitlistNode {
    private Order order;
    protected WaitlistNode next;
    protected WaitlistNode prev;

    public WaitlistNode(Order order) {
        this.order = order;
        this.next = null;
        this.prev = null;
    }

    public Order getOrder() { return order; }
    public boolean equals(WaitlistNode other) {
		return this.order.getOrderNumber().equals(other.getOrder().getOrderNumber());
	}
    
    @Override
    public String toString() {
		return "WaitlistNode{" +
				"order=" + order +
				'}';
	}
}