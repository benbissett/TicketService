package testProject;

import java.util.ArrayList;

public class SeatHold {
	private int id;
	private ArrayList<Seat> seats;
	private String customerEmail;
	private long startTime;
	
	/**
	 * Constructor for SeatHold object
	 * Sets the startTime for the object
	 * 
	 * @param id number for the order
	 * @param seats List of seats being held
	 * @param customerEmail email of person holding seats
	 */
	public SeatHold(int id, ArrayList<Seat> seats, String customerEmail) {
		this.id = id;
		this.seats = seats;
		this.customerEmail = customerEmail;
		startTime = System.nanoTime();
	}
	
	/**
	 * 
	 * @return id number for the order
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * 
	 * @return list of all seats being held by this SeatHold
	 */
	public ArrayList<Seat> getSeats() {
		return seats;
	}
	
	/**
	 * 
	 * @return email of person who is holding the seats
	 */
	public String getEmail() {
		return customerEmail;
	}
	
	/**
	 * 
	 * @return time when this object was created
	 */
	public long getStartTime() {
		return startTime;
	}
}