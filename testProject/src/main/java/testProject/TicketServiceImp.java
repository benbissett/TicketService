package testProject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class TicketServiceImp implements TicketService {
	final double EXPIRATION = 5;
	int rows;
	int cols;
	private int holdId = 1;
	private int seatsAvailable;
	
	//list of all tickets that currently exist
	public LinkedList<SeatHold> holdList = new LinkedList<SeatHold>();
	
	//map of all seats using the seat number as the key
	public HashMap<Integer, Seat> seats = new HashMap<Integer, Seat>();
	
	//map of all reservations using the SeatHold id as the key
	public HashMap<Integer, SeatHold> reserved = new HashMap<Integer, SeatHold>();
	
	/**
	 * Constructor that creates all the seats in the theater and puts them in the seats hashmap
	 * 
	 * @param rows the number of rows in the theater
	 * @param cols the number of seats in each row
	 */
	public TicketServiceImp(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		this.seatsAvailable = rows*cols;
		for (int i = 0; i < rows; i++) {
			for (int j=1; j <= cols; j++) {
				seats.put(i*cols+j, new Seat(i*cols+j));
			}
		}
	}
	
	/**
	 * The number of seats in the venue that are neither held nor reserved
	 * Also checks all of the orders in the holdList to see if any are expired,
	 * and makes the seats on hold from that order available again
	 * 
	 *@return the number of tickets available in the venue 
	 */ 
	public int numSeatsAvailable() {
		for (int i=0; i < holdList.size(); i++) {
			long elapsedTime = System.nanoTime() - holdList.get(i).getStartTime();
			if ((elapsedTime / 1000000000.0) > EXPIRATION) {
				ArrayList<Seat> list = holdList.get(i).getSeats();
				for (int j=0; j<list.size(); j++) {
					list.get(j).setAvailable(true);
					seatsAvailable += 1;
				}
				holdList.remove(i);
			}
		}
		
		return seatsAvailable;
	}
	
	/**
	 * Find and hold the best available seats for a customer
	 * If there aren't enough consecutive seats it still finds and holds enough seats
	 * 
	 * @param numSeats the number of seats to find and hold
	 * @param customerEmail unique identifier for the customer
	 * @return a seatHold object identifying the specific seats and related information or null if not enough seats available
	 */
	public SeatHold findAndHoldSeats(int numSeats, String customerEmail) {
		//tries to fulfill request with consecutive seats first
		if (numSeatsAvailable() >= numSeats) {
			for (int i=0; i < rows; i++) {
				ArrayList<Seat> order = new ArrayList<Seat>();
				for (int j=1; j <= cols; j++) {
					Seat curr = seats.get(i*cols+j);
					if (curr.isAvailable() && curr.isReserved() == false) {
						order.add(curr);
					}
					else {
						order.clear();
					}
					
					if (order.size() == numSeats) {
						seatsAvailable -= numSeats;
						SeatHold hold = new SeatHold(holdId++, order, customerEmail);
						holdList.add(hold);
						System.out.print("Your seats are ");
						for (int index=0; index < order.size(); index++) {
							order.get(index).setAvailable(false);
							System.out.print(order.get(index).getId() + " ");
						}
						System.out.println("");
						return hold;
					}
				}
			}
			
			//didn't find enough consecutive seats so now it just fulfills the request with non consecutive seats
			ArrayList<Seat> order = new ArrayList<Seat>();
			for (int i=0; i < rows; i++) {
				for (int j=1; j <= cols; j++) {
					Seat curr = seats.get(i*cols+j);
					if (curr.isAvailable() && curr.isReserved() == false) {
						order.add(curr);
					}
		
					if (order.size() == numSeats) {
						seatsAvailable -= numSeats;
						SeatHold hold = new SeatHold(holdId++, order, customerEmail);
						holdList.add(hold);
						System.out.print("Your seats are ");
						for (int index=0; index < order.size(); index++) {
							order.get(index).setAvailable(false);
							System.out.print(order.get(index).getId() + " ");
						}
						System.out.println("");
						System.out.println("They are not consecutive seats");
						return hold;
					}
				}
			}
		}
		System.out.println("You tried to reserve too many seats. Only " + seatsAvailable + " seats remain");
		return null;
	}
	
	/**
	 * Commit seats held for a specific customer
	 * Same as numSeatsAvailable() this function also checks to make sure the order trying to be reserved hasn't expired
	 * if it has expired it makes all the seats in the order available again
	 * 
	 * @param seatHoldId the seat hold identifier
	 * @param customerEmail the email address of the customer to which the seat hold is assigned
	 * @return a reservation confirmation code
	 */
	public String reserveSeats(int seatHoldId, String customerEmail) {
		for (int i=0; i < holdList.size(); i++) {
			if (holdList.get(i).getId() == seatHoldId) {
				long elapsedTime = System.nanoTime() - holdList.get(i).getStartTime();
				//if expired makes seats available and gets rid of order
				if ((elapsedTime / 1000000000.0) > EXPIRATION) {
					ArrayList<Seat> list = holdList.get(i).getSeats();
					for (int j=0; j<list.size(); j++) {
						list.get(j).setAvailable(true);
						seatsAvailable += 1;
					}
					holdList.remove(i);
					return("Order number " + seatHoldId + " timed out or does not exist");
				}
				//if not expired then reserve seats and send confirmation
				else {
					reserved.put(seatHoldId, holdList.get(i));
					ArrayList<Seat> list = holdList.get(i).getSeats();
					for (int index=0; index < list.size(); index++) {
						list.get(index).reserve();
					}
					holdList.remove(i);
					return("Order number " + seatHoldId + " Reservation Confirmed");
				}
			}
		}
		return("Order number " + seatHoldId + " timed out or does not exist");
	}  
}

