package testProject;

import static org.junit.Assert.*;

import org.junit.Test;

public class Tests {

	@Test
	/**
	 * tests to make sure the list of available seats is full when created
	 */
	public void test1() {
		TicketServiceImp test = new TicketServiceImp(9, 33);
		assertEquals("Something wrong", 297, test.seats.size());
	}
	
	@Test
	/**
	 * Tests the findAndHoldSeats function to make sure it removes the correct number of seats from available seats,
	 * sets the seats as unavailable, and adds a order to the holdList
	 */
	public void test2() {
		TicketServiceImp test = new TicketServiceImp(9, 33);
		test.findAndHoldSeats(4, "email");
		assertEquals("Didn't remove seats from number of available", 293, test.numSeatsAvailable());
		assertEquals("Didn't change availability of seats", false, test.seats.get(1).isAvailable());
		assertEquals("Didn't add order to holdList", 1,	test.holdList.size());
	}
	
	@Test
	/**
	 * Further tests the findAndHoldSeats function to make sure that it doesn't try to use seats that are already on hold.
	 */
	public void test3() {
		TicketServiceImp test = new TicketServiceImp(9,33);
		test.findAndHoldSeats(4, "email");
		test.findAndHoldSeats(4, "email2");
		assertEquals("Didn't remove seats from number of available", 289, test.numSeatsAvailable());
		assertEquals("Didn't change availability of seats", false, test.seats.get(5).isAvailable());
	}
	
	@Test
	/**
	 * Tests the reserveSeats function to make sure that it properly reserves seats and marks seats as reserved.
	 */
	public void test4() {
		TicketServiceImp test = new TicketServiceImp(9,33);
		SeatHold order = test.findAndHoldSeats(4, "email");
		test.reserveSeats(order.getId(), "email");
		assertEquals("Didn't remove seats from number of available", 293, test.numSeatsAvailable());
		assertEquals("Didn't change availability of seats", false, test.seats.get(1).isAvailable());
		assertEquals("Didn't reserve order correctly", order, test.reserved.get(order.getId()));
		assertEquals("Didn't remove order from holdList", 0, test.holdList.size());
		assertEquals("Didn't reserve the seats properly", true, test.seats.get(1).isReserved());
	}
	
	@Test
	/**
	 * Tests if orders expire after a set number of seconds and tests if seats are made available again when a order expires
	 * Also tests that a order can't be reserved if it has expired, and if seats that were made available again after a order expired are 
	 * able to be put on hold in a different order
	 */
	public void test5() {
		TicketServiceImp test = new TicketServiceImp(9,33);
		SeatHold order = test.findAndHoldSeats(4, "email");
		
		try {
			Thread.sleep(6000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertEquals("Didn't add seats back to number of available", 297, test.numSeatsAvailable());
		assertEquals("Didn't set seats as available", true, test.seats.get(1).isAvailable());
		
		String response = test.reserveSeats(order.getId(), "email");
		assertEquals("Didn't time out order", "Order number " + order.getId() + " timed out or does not exist", response);
		
		test.findAndHoldSeats(4, "email");
		assertEquals("Didn't set seats as unavailable", false, test.seats.get(1).isAvailable());
		
	}
	
	@Test
	/**
	 * Tests if reserveSeat doesn't reserve expired orders and makes the seats available again
	 */
	public void test6() {
		TicketServiceImp test = new TicketServiceImp(9,33);
		SeatHold order = test.findAndHoldSeats(4, "email");
		
		try {
			Thread.sleep(6000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		String response = test.reserveSeats(order.getId(), "email");
		assertEquals("Didn't set seats as available", true, test.seats.get(1).isAvailable());
		assertEquals("Didn't time out order", "Order number " + order.getId() + " timed out or does not exist", response);
		
	}
	
	@Test
	/**
	 * Tests to make sure that even if the seats aren't consecutive the findAndHoldSeats function still finds enough seats if 
	 * there are enough seats available
	 * Also tests that you can't put on hold more seats than are currently available 
	 */
	public void test7() {
		TicketServiceImp test = new TicketServiceImp(2, 3);
		SeatHold order = test.findAndHoldSeats(4, "email");
		assertEquals("Didn't reserve correct seats", 2, test.numSeatsAvailable());
		
		test.reserveSeats(order.getId(), "email");
		assertEquals("Didn't reserve seats", 1, test.reserved.size());
		
		SeatHold order2 = test.findAndHoldSeats(4, "email2");
		assertEquals("Allows more seats than are available to be reserved", null, order2);
		
		test.findAndHoldSeats(2, "email2");
		assertEquals("Didn't set seats as unavailable", false, test.seats.get(5).isAvailable());
		assertEquals(0, test.numSeatsAvailable());
	}
}

