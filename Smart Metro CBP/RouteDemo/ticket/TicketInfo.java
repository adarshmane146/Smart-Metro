package ticket;

public class TicketInfo
{
	private String passengerName;
	private String source;
	private String destination;
	private int numberOfPassengers;
	private int distance;
	private int fare;
	private String paymentMethod;
	private String transactionId;
	private String ticketId;

	public TicketInfo(String passengerName, String source, String destination,
					  int numberOfPassengers, int distance, int fare,
					  String paymentMethod, String transactionId,
					  String ticketId)
	{
		this.passengerName = passengerName;
		this.source = source;
		this.destination = destination;
		this.numberOfPassengers = numberOfPassengers;
		this.distance = distance;
		this.fare = fare;
		this.paymentMethod = paymentMethod;
		this.transactionId = transactionId;
		this.ticketId = ticketId;
	}

	public String getPassengerName()
	{
		return passengerName;
	}

	public String getSource()
	{
		return source;
	}

	public String getDestination()
	{
		return destination;
	}

	public int getNumberOfPassengers()
	{
		return numberOfPassengers;
	}

	public int getDistance()
	{
		return distance;
	}

	public int getFare()
	{
		return fare;
	}

	public String getPaymentMethod()
	{
		return paymentMethod;
	}

	public String getTransactionId()
	{
		return transactionId;
	}

	public String getTicketId()
	{
		return ticketId;
	}
}
