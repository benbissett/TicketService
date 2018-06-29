package testProject;

public class Seat {
	final double EXPIRATION = 5;
	private int id;
	
	private boolean available = true;
	
	private boolean reserved = false;
	
	
	/**
	 * Contructor for seat object
	 * 
	 * @param id seat number
	 */
	public Seat(int id) {
		this.id = id;
	}
	
	/**
	 * 
	 * @return true if seat is available false if seat is currently on hold
	 */
	public boolean isAvailable() {
			return available;
	}
	
	/**
	 * Changes the availability of the seat if it hasn't been reserved 
	 * @param available true or false 
	 */
	public void setAvailable(boolean available) {
		if (reserved == false) {
			this.available = available;
		}
	}
	
	/**
	 * marks the seat is reserved
	 */
	public void reserve() {
		reserved = true;
	}
	
	/**
	 * 
	 * @return true if seat is reserved false otherwise
	 */
	public boolean isReserved() {
		return reserved;
	}
	
	/**
	 * 
	 * @return seat number
	 */
	public int getId() {
		return id;
	}
}
