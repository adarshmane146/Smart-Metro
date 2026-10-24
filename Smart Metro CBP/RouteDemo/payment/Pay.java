package payment;

import java.util.InputMismatchException;
import java.util.Scanner;

import r1.RouteServices;
import ticket.TicketInfo;
import ticket.TicketHistory;

public class Pay
{
	public static void toPayMenu(TicketHistory ticketHistory)
	{
		Scanner sc = new Scanner(System.in);
		RouteServices routeServices = new RouteServices();

		int tries = 3;

		while(tries > 0)
		{
			String sourceInput;
			String destinationInput;

			try
			{
				System.out.println();
				System.out.println("===============================================");
				System.out.println("\t\tBOOK TICKET");
				System.out.println("===============================================");

				System.out.print("Enter Source : ");
				sourceInput = sc.nextLine();

				if(sourceInput.isBlank())
					throw new IllegalArgumentException("Source station cannot be empty.");

				String sourceInfo = routeServices.findStation(sourceInput);

				if(sourceInfo == null)
					throw new IllegalArgumentException("Invalid source station.");

				System.out.print("Enter Destination : ");
				destinationInput = sc.nextLine();

				if(destinationInput.isBlank())
					throw new IllegalArgumentException("Destination station cannot be empty.");

				String destinationInfo = routeServices.findStation(destinationInput);

				if(destinationInfo == null)
					throw new IllegalArgumentException("Invalid destination station.");

				String sourceLine = sourceInfo.replaceAll("\\d", "");
				String destinationLine = destinationInfo.replaceAll("\\d", "");

				int sourceIndex = Integer.parseInt(sourceInfo.replaceAll("\\D", ""));
				int destinationIndex = Integer.parseInt(destinationInfo.replaceAll("\\D", ""));

				String sourceStation = routeServices.getStationName(sourceLine, sourceIndex);
				String destinationStation = routeServices.getStationName(destinationLine, destinationIndex);

				if(sourceStation.equals(destinationStation))
					throw new IllegalArgumentException("Source and destination cannot be same.");

				int distance;
				int farePerPassenger;

				System.out.println();

				if(sourceLine.equals(destinationLine))
				{
					routeServices.displayRoute(
						sourceStation,
						destinationStation,
						sourceLine
					);

					distance = routeServices.calculateDistance(
						sourceStation,
						destinationStation,
						sourceLine
					);
				}
				else
				{
					String mix = sourceLine + destinationLine;

					String midStation = routeServices.getInterchangeStation(mix);

					if(midStation == null)
						throw new IllegalArgumentException("Route is not available between these stations.");

					routeServices.displayRoute(
						sourceStation,
						destinationStation,
						sourceLine,
						destinationLine,
						mix,
						midStation
					);

					int firstDistance = routeServices.calculateDistance(
						sourceStation,
						midStation,
						sourceLine
					);

					String secondInterchange = routeServices.getDestinationInterchange(
						mix,
						destinationLine,
						midStation
					);

					int secondDistance = routeServices.calculateDistance(
						secondInterchange,
						destinationStation,
						destinationLine
					);

					if(firstDistance == -1 || secondDistance == -1)
						throw new IllegalArgumentException("Unable to calculate route distance.");

					distance = firstDistance + secondDistance;
				}

				if(distance == -1)
					throw new IllegalArgumentException("Unable to calculate route distance.");

				farePerPassenger = routeServices.calculateFare(distance);

				int numberOfPassengers;

				while(true)
				{
					try
					{
						System.out.print("Enter Number of Passengers : ");

						numberOfPassengers = sc.nextInt();
						sc.nextLine();

						if(numberOfPassengers <= 0)
							throw new IllegalArgumentException(
								"Number of passengers must be greater than 0."
							);

						break;
					}
					catch(InputMismatchException e)
					{
						System.out.println("Please enter a valid number.");
						sc.nextLine();
					}
					catch(IllegalArgumentException e)
					{
						System.out.println(e.getMessage());
					}
				}

				String passengerName;

				while(true)
				{
					try
					{
						System.out.print("Enter Passenger Name : ");
						passengerName = sc.nextLine();

						if(passengerName.isBlank())
							throw new IllegalArgumentException(
								"Passenger name cannot be empty."
							);

						if(!passengerName.matches("[a-zA-Z ]+"))
							throw new IllegalArgumentException(
								"Passenger name must contain only letters."
							);

						break;
					}
					catch(IllegalArgumentException e)
					{
						System.out.println(e.getMessage());
					}
				}

				int totalFare = farePerPassenger * numberOfPassengers;

				System.out.println();
				System.out.println("-----------------------------------------------");
				System.out.println("Source               : " + sourceStation);
				System.out.println("Destination          : " + destinationStation);
				System.out.println("Distance             : " + distance + " stations");
				System.out.println("Fare Per Passenger   : \u20b9" + farePerPassenger);
				System.out.println("Number of Passengers : " + numberOfPassengers);
				System.out.println("Total Fare           : \u20b9" + totalFare);
				System.out.println("-----------------------------------------------");

				while(true)
				{
					try
					{
						System.out.println();
						System.out.println("1. Make Payment");
						System.out.println("2. Back");

						System.out.print("Enter your choice : ");

						int choice = sc.nextInt();
						sc.nextLine();

						if(choice == 1)
						{
							PaymentDetails paymentDetails = Payment.paymentOptions(sc);

							if(paymentDetails != null)
							{
								String ticketId =
									"SM" + (10000 + ticketHistory.getCount() + 1);

								TicketInfo ticket = new TicketInfo(
									passengerName,
									sourceStation,
									destinationStation,
									numberOfPassengers,
									distance,
									totalFare,
									paymentDetails.getPaymentMethod(),
									paymentDetails.getTransactionId(),
									ticketId
								);

								ticketHistory.addTicket(ticket);

								System.out.println();
								System.out.println("===============================================");
								System.out.println("\tTicket Booking Successful");
								System.out.println("===============================================");

								System.out.println("Ticket ID            : " + ticketId);
								System.out.println("Passenger Name       : " + passengerName);
								System.out.println("Source               : " + sourceStation);
								System.out.println("Destination          : " + destinationStation);
								System.out.println("Number of Passengers : " + numberOfPassengers);
								System.out.println("Distance             : " + distance + " stations");
								System.out.println("Fare Per Passenger   : " + farePerPassenger + "/-");
								System.out.println("Total Fare           : " + totalFare + "/-");
								System.out.println("Payment Method       : " +
									paymentDetails.getPaymentMethod());
								System.out.println("Transaction ID       : " +
									paymentDetails.getTransactionId());
							}

							break;
						}
						else if(choice == 2)
						{
							return;
						}
						else
						{
							throw new IllegalArgumentException(
								"Please choose 1 or 2."
							);
						}
					}
					catch(InputMismatchException e)
					{
						System.out.println("Please enter a number.");
						sc.nextLine();
					}
					catch(IllegalArgumentException e)
					{
						System.out.println(e.getMessage());
					}
				}

				break;
			}
			catch(IllegalArgumentException e)
			{
				System.out.println();
				System.out.println(e.getMessage());

				tries--;

				if(tries > 0)
					System.out.printf("You have %d attempts only.%n", tries);
			}
		}

		if(tries == 0)
		{
			System.out.println();
			System.out.println("Maximum attempts completed.");
		}
	}
}
