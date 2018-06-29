Ben Bissett

I assumed that the closer the seat is to the front the better the seat is which means that the best available seat is just the
seat with the lowest number available.

I assumed that even if I couldn't find enough consecutive seats to fulfill a request I should still return a SeatHold on enough seats
even if they aren't all next to each other. I did this, because the user can let the SeatHold time out if they don't want seats that aren't next
to each other. To let the person know which seats they got I decided to print out the seat numbers of each seat, and if they aren't consecutive I
also sent out a message saying the seats aren't consecutive to make it easier for the user to decide if they want the seats or not.

I assumed SeatHolds would time out after 5 seconds for the purpose of testing.

I assumed the String returned by the reserveSeats method is just a string containing the SeatHoldId and a confirmation message.

I used Maven as the build tool so in order to build the solution you need to navigate to the directory for this project in the cmd line and then
type mvn compile. For tests simply type mvn test after compiling the project.