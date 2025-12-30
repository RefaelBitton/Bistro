package entities;

import java.io.Serializable;
/**
 * A class representing a table in the bistro
 */
public class Table implements Serializable, Comparable<Table> {

	private static final long serialVersionUID = 1L;
	/** the table's id*/
	private int id;
	/** the table's capacity*/
	private int capacity;
	/** whether the table is taken or not*/
	private boolean isTaken;
	public Table(int id, int capacity, boolean isTaken) {
		super();
		this.id = id;
		this.capacity = capacity;
		this.isTaken = isTaken;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public boolean isTaken() {
		return isTaken;
	}
	public void setTaken(boolean isTaken) {
		this.isTaken = isTaken;
	}
	public int getId() {
		return id;
	}
	@Override
	public int compareTo(Table o) {
		if (this.capacity>o.capacity)
			return 1;
		else if (this.capacity<o.capacity)
			return -1;
		else
		return 0;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Table other = (Table) obj;
		if (id != other.getId())
			return false;
		return true;
	}
	
	public String toString() {
		return "Table Number: " + id + ", Capacity: " + capacity;
	}
	
}
