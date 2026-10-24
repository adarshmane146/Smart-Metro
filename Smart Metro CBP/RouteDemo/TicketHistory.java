package ticket;

public class TicketHistory
{
	private TicketInfo[] tickets = new TicketInfo[100];
	private int count = 0;

	public void addTicket(TicketInfo ticket)
	{
		if(count >= tickets.length)
		{
			System.out.println("Ticket History is full.");
			return;
		}

		tickets[count] = ticket;
		count++;
	}

	public int getCount()
	{
		return count;
	}

	public void viewHistory()
	{
		if(count == 0)
		{
			System.out.println();
			System.out.println("No tickets booked yet.");
			return;
		}

		System.out.println();
		System.out.println("===============================================");
		System.out.println("\t\tTicket History");
		System.out.println("===============================================");

		for(int i = 0; i < count; i++)
		{
			TicketInfo ticket = tickets[i];

			System.out.println();
			System.out.println("Ticket " + (i + 1));
			System.out.println("-----------------------------------------------");

			System.out.println("Ticket ID            : " + ticket.getTicketId());
			System.out.println("Passenger Name       : " + ticket.getPassengerName());
			System.out.println("Source               : " + ticket.getSource());
			System.out.println("Destination          : " + ticket.getDestination());
			System.out.println("Number of Passengers : " + ticket.getNumberOfPassengers());
			System.out.println("Distance             : " + ticket.getDistance() + " stations");
			System.out.println("Total Fare           : " + ticket.getFare() + "/-");
			System.out.println("Payment Method       : " + ticket.getPaymentMethod());
			System.out.println("Transaction ID       : " + ticket.getTransactionId());

			System.out.println("-----------------------------------------------");
		}
	}
}