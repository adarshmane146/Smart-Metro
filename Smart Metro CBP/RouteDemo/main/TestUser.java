package main;

import java.util.Scanner;
import java.util.InputMismatchException;

import enums.UserRole;
import exception.InvalidUserException;
import exception.UserNotFoundException;
import r1.RouteOperations;
import r1.RouteServices;
import user.User;
import user.UserOperations;
import user.UserServices;

import payment.Pay;
import ticket.TicketHistory;

class TestUser {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		UserOperations userService = new UserServices();
		RouteOperations routeService = new RouteServices();
		User loggedUser = null;

		while (true) {

			System.out.println();
			System.out.println("================================");
			System.out.println("          SMART METRO");
			System.out.println("================================");
			System.out.println("1. Register");
			System.out.println("2. Login");
			System.out.println("3. Exit");
			System.out.print("Enter choice : ");

			int choice;
			try {
				choice = sc.nextInt();
				sc.nextLine();
			} catch (InputMismatchException e) {
				System.out.println("Invalid input. Please enter a number.");
				sc.nextLine();
				continue;
			}

			switch (choice) {

				case 1:
					registerUser(sc, userService);
					break;

				case 2:
					loggedUser = loginMenu(sc, userService);
					if (loggedUser != null) {
						userMenu(sc, userService, routeService, loggedUser);
						loggedUser = null;
					}
					break;

				case 3:
					System.out.println();
					System.out.println("================================");
					System.out.println("Thank you for using Smart Metro.");
					System.out.println("================================");
					sc.close();
					return;

				default:
					System.out.println();
					System.out.println("Invalid choice.");
			}
		}
	}

	public static void registerUser(Scanner sc, UserOperations userService) {


		System.out.println();
		System.out.println("================================");
		System.out.println("           REGISTER");
		System.out.println("================================");
		System.out.println("1. Continue");
		System.out.println("2. Back");
		System.out.print("Enter choice : ");

		int choice;
			try {
				choice = sc.nextInt();
				sc.nextLine();
			} catch (InputMismatchException e) {
				System.out.println("Invalid input. Please enter a number.");
				sc.nextLine();
				return;
			}

		if (choice == 2) {
			return;
		}

		if (choice != 1) {
			System.out.println("Invalid choice.");
			return;
		}

		System.out.print("Enter Name : ");
		String name = sc.nextLine();

		System.out.print("Enter Email : ");
		String email = sc.nextLine();

		System.out.print("Enter Mobile : ");
		long mobile;
		try {
			mobile = sc.nextLong();
			sc.nextLine();
		} catch (InputMismatchException e) {
			System.out.println("Invalid mobile number. Please enter a valid 10-digit number.");
			sc.nextLine();
			return;
		}


		System.out.print("Enter Password : ");
		String password = sc.nextLine();

		User user = new User(name, email, mobile, password, UserRole.USER);

		try {
			System.out.println(userService.registerUser(user));
			System.out.println("Please login to continue.");
		} catch (InvalidUserException e) {
			System.out.println("Registration Failed : " + e.getMessage());
		}
	}

	public static User loginMenu(Scanner sc, UserOperations userService) {

		if (userService instanceof UserServices && ((UserServices) userService).getUserCount() == 0) {
			System.out.println();
			System.out.println("No users are registered.");
			System.out.println("Please register first.");
			return null;
		}

		while (true) {

			System.out.println();
			System.out.println("================================");
			System.out.println("             LOGIN");
			System.out.println("================================");
			System.out.println("1. Login by User ID");
			System.out.println("2. Login by Email");
			System.out.println("3. Forgot Password");
			System.out.println("4. Back");
			System.out.print("Enter choice : ");

			int choice;
			try {
				choice = sc.nextInt();
				sc.nextLine();
			} catch (InputMismatchException e) {
				System.out.println("Invalid input. Please enter a number.");
				sc.nextLine();
				continue;
			}

			switch (choice) {

				case 1:
					User idUser = loginByUserId(sc, userService);
					if (idUser != null) return idUser;
					break;

				case 2:
					User emailUser = loginByEmail(sc, userService);
					if (emailUser != null) return emailUser;
					break;

				case 3:
					forgotPassword(sc, userService);
					break;

				case 4:
					return null;

				default:
					System.out.println("Invalid choice.");
			}
		}
	}

	public static User loginByUserId(Scanner sc, UserOperations userService) {

		System.out.print("Enter User ID : ");
		int userId;
		try {
			userId = sc.nextInt();
			sc.nextLine();
		} catch (InputMismatchException e) {
			System.out.println("Invalid User ID. Please enter a number.");
			sc.nextLine();
			return null;
		}

		if (!userService.userExists(userId)) {
			System.out.println("User ID not found.");
			return null;
		}


		for (int attempt = 1; attempt <= 3; attempt++) {

			System.out.print("Enter Password : ");
			String password = sc.nextLine();

			try {
				User user = userService.loginUser(userId, password);
				System.out.println("Login Successful.");
				System.out.println("Welcome, " + user.getName() + "!");
				return user;
			} catch (InvalidUserException e) {
				System.out.println("Invalid password.");
				if (attempt < 3) System.out.println("Attempts remaining : " + (3 - attempt));
				else System.out.println("Maximum login attempts exceeded.");
			} catch (UserNotFoundException e) {
				System.out.println("User not found.");
				return null;
			}
		}

		return null;
	}

	public static User loginByEmail(Scanner sc, UserOperations userService) {

		System.out.print("Enter Email : ");
		String email = sc.nextLine();

		if (!userService.emailExists(email)) {
			System.out.println("Email not registered.");
			return null;
		}

		for (int attempt = 1; attempt <= 3; attempt++) {

			System.out.print("Enter Password : ");
			String password = sc.nextLine();

			try {
				User user = userService.loginUser(email, password);
				System.out.println("Login Successful.");
				System.out.println("Welcome, " + user.getName() + "!");
				return user;
			} catch (InvalidUserException e) {
				System.out.println("Invalid password.");
				if (attempt < 3) System.out.println("Attempts remaining : " + (3 - attempt));
				else System.out.println("Maximum login attempts exceeded.");
			} catch (UserNotFoundException e) {
				System.out.println("User not found.");
				return null;
			}
		}

		return null;
	}

	public static void forgotPassword(Scanner sc, UserOperations userService) {

		System.out.println();
		System.out.println("================================");
		System.out.println("        FORGOT PASSWORD");
		System.out.println("================================");
		System.out.println("1. Continue");
		System.out.println("2. Back");
		System.out.print("Enter choice : ");

		int choice;
			try {
				choice = sc.nextInt();
				sc.nextLine();
			} catch (InputMismatchException e) {
				System.out.println("Invalid input. Please enter a number.");
				sc.nextLine();
				return;
			}

		if (choice == 2) {
			return;
		}

		if (choice != 1) {
			System.out.println("Invalid choice.");
			return;
		}

		System.out.print("Enter Email : ");
		String email = sc.nextLine();

		System.out.print("Enter Mobile : ");
		long mobile;
		try {
			mobile = sc.nextLong();
			sc.nextLine();
		} catch (InputMismatchException e) {
			System.out.println("Invalid mobile number. Please enter a valid 10-digit number.");
			sc.nextLine();
			return;
		}

		User user = null;

		for (int i = 100; i <= 999; i++) {
			try {
				User temp = userService.getUserById(i);
				if (temp.getEmail().equalsIgnoreCase(email) && temp.getMobile() == mobile) {
					user = temp;
					break;
				}
			} catch (UserNotFoundException e) {
			}
		}

		if (user == null) {
			System.out.println("Invalid email or mobile number.");
			return;
		}

		System.out.print("Enter New Password : ");
		String password = sc.nextLine();

		try {
			userService.updatePassword(user.getUserID(), password);
			System.out.println("Password updated successfully.");
		} catch (InvalidUserException e) {
			System.out.println("Password update failed : " + e.getMessage());
		} catch (UserNotFoundException e) {
			System.out.println("User not found.");
		}
	}

	public static void userMenu(Scanner sc, UserOperations userService, RouteOperations routeService, User user) {

		boolean running = true;
		TicketHistory ticketHistory = new TicketHistory();

		while (running) {

			System.out.println();
			System.out.println("================================");
			System.out.println("           USER MENU");
			System.out.println("================================");
			System.out.println("1. View Profile");
			System.out.println("2. Metro Services");
			System.out.println("3. Account Operations");
			System.out.println("4. Book Ticket");
			System.out.println("5. Ticket History");
			System.out.println("6. Logout");
			System.out.print("Enter choice : ");

			int choice;
			try {
				choice = sc.nextInt();
				sc.nextLine();
			} catch (InputMismatchException e) {
				System.out.println("Invalid input. Please enter a number.");
				sc.nextLine();
				continue;
			}

			switch (choice) {

				case 1:
					profileMenu(sc, userService, user);
					break;

				case 2:
					metroMenu(sc, routeService);
					break;

				case 3:
					if (!accountMenu(sc, userService, user)) running = false;
					break;

				case 4:
					Pay.toPayMenu(ticketHistory);
					break;

				case 5:
					ticketHistory.viewHistory();
					break;

				case 6:
					System.out.println("Logout Successful.");
					running = false;
					break;

				default:
					System.out.println("Invalid choice.");
			}
		}
	}

	public static void profileMenu(Scanner sc, UserOperations userService, User user) {

		while (true) {

			System.out.println();
			System.out.println("================================");
			System.out.println("             PROFILE");
			System.out.println("================================");
			System.out.println("1. Display Profile");
			System.out.println("2. Back");
			System.out.print("Enter choice : ");

			int choice;
			try {
				choice = sc.nextInt();
				sc.nextLine();
			} catch (InputMismatchException e) {
				System.out.println("Invalid input. Please enter a number.");
				sc.nextLine();
				continue;
			}

			switch (choice) {

				case 1:

					try {
						userService.displayProfile(user.getUserID());
					} catch (UserNotFoundException e) {
						System.out.println("User not found.");
					}

					break;

				case 2:

					return;

				default:

					System.out.println("Invalid choice.");
			}
		}
	}

	public static boolean accountMenu(Scanner sc, UserOperations userService, User user) {

		while (true) {

			System.out.println();
			System.out.println("================================");
			System.out.println("       ACCOUNT OPERATIONS");
			System.out.println("================================");
			System.out.println("1. Update Name");
			System.out.println("2. Update Email");
			System.out.println("3. Update Mobile");
			System.out.println("4. Update Password");
			System.out.println("5. Verify Password");
			System.out.println("6. Delete Account");
			System.out.println("7. Back");
			System.out.print("Enter choice : ");

			int choice;
			try {
				choice = sc.nextInt();
				sc.nextLine();
			} catch (InputMismatchException e) {
				System.out.println("Invalid input. Please enter a number.");
				sc.nextLine();
				continue;
			}

			switch (choice) {

				case 1:
					System.out.print("Enter New Name : ");
					String name = sc.nextLine();
					try {
						userService.updateName(user.getUserID(), name);
						System.out.println("Name updated successfully.");
					} catch (InvalidUserException e) {
						System.out.println("Update Failed : " + e.getMessage());
					} catch (UserNotFoundException e) {
						System.out.println("User not found.");
					}
					break;

				case 2:
					System.out.print("Enter New Email : ");
					String email = sc.nextLine();
					try {
						userService.updateEmail(user.getUserID(), email);
						System.out.println("Email updated successfully.");
					} catch (InvalidUserException e) {
						System.out.println("Update Failed : " + e.getMessage());
					} catch (UserNotFoundException e) {
						System.out.println("User not found.");
					}
					
					break;

				case 3:

					System.out.print("Enter New Mobile : ");
					long mobile;
					try {
						mobile = sc.nextLong();
						sc.nextLine();
					} catch (InputMismatchException e) {
						System.out.println("Invalid mobile number. Please enter a valid 10-digit number.");
						sc.nextLine();
						break;
					}
					try {
						userService.updateMobile(user.getUserID(), mobile);
						System.out.println("Mobile updated successfully.");
					} catch (InvalidUserException e) {
						System.out.println("Update Failed : " + e.getMessage());
					} catch (UserNotFoundException e) {
						System.out.println("User not found.");
					}
					break;

				case 4:
					System.out.print("Enter New Password : ");
					String password = sc.nextLine();
					try {
						userService.updatePassword(user.getUserID(), password);
						System.out.println("Password updated successfully.");
					} catch (InvalidUserException e) {
						System.out.println("Update Failed : " + e.getMessage());
					} catch (UserNotFoundException e) {
						System.out.println("User not found.");
					}
					break;

				case 5:
					System.out.print("Enter Password : ");
					String verifyPassword = sc.nextLine();
					try {
						if (userService.verifyPassword(user.getUserID(), verifyPassword)) {
							System.out.println("Password Verified.");
						} else {
							System.out.println("Invalid Password.");
						}
					} catch (UserNotFoundException e) {
						System.out.println("User not found.");
					}
					break;

				case 6:
					System.out.print("Enter Password to Delete Account : ");
					String deletePassword = sc.nextLine();
					try {
						if (!userService.verifyPassword(user.getUserID(), deletePassword)) {
							System.out.println("Invalid Password.");
							break;
						}
						if (userService.deleteUser(user.getUserID())) {
							System.out.println("Account deleted successfully.");
							return false;
						}
					} catch (UserNotFoundException e) {
						System.out.println("User not found.");
					}
					break;

				case 7:
					return true;

				default:
					System.out.println("Invalid choice.");
			}
		}
	}

	public static void metroMenu(Scanner sc, RouteOperations routeService) {

		boolean running = true;

		while (running) {

			System.out.println();
			System.out.println("================================");
			System.out.println("         METRO SERVICES");
			System.out.println("================================");
			System.out.println("1. View Red Line Stations");
			System.out.println("2. View Blue Line Stations");
			System.out.println("3. View Green Line Stations");
			System.out.println("4. View Metro Map");
			System.out.println("5. Find Route");
			System.out.println("6. Back");
			System.out.print("Enter choice : ");

			int choice;
			try {
				choice = sc.nextInt();
				sc.nextLine();
			} catch (InputMismatchException e) {
				System.out.println("Invalid input. Please enter a number.");
				sc.nextLine();
				continue;
			}

			switch (choice) {

				case 1:
					routeService.viewRedLineStations();
					break;

				case 2:
					routeService.viewBlueLineStations();
					break;

				case 3:
					routeService.viewGreenLineStations();
					break;

				case 4:
					routeService.displayMap();
					break;

				case 5:
					routeMenu(sc, routeService);
					break;

				case 6:
					running = false;
					break;

				default:
					System.out.println("Invalid choice.");
			}
		}
	}

	public static void routeMenu(Scanner sc, RouteOperations routeService)
	{
	while(true)
	{
		System.out.println();
		System.out.println("================================");
		System.out.println("           FIND ROUTE");
		System.out.println("================================");
		System.out.println("1. Search Route");
		System.out.println("2. Back");
		System.out.print("Enter choice : ");

		int choice;

		try
		{
			choice = sc.nextInt();
			sc.nextLine();
		}
		catch(InputMismatchException e)
		{
			System.out.println("Invalid input. Please enter a number.");
			sc.nextLine();
			continue;
		}

		switch(choice)
		{
			case 1:
				findRoute(sc, routeService);
				break;
			case 2:
				return;
			default:
				System.out.println("Invalid choice.");
		}
	}
}

public static void findRoute(Scanner sc, RouteOperations routeService)
{
	int attempts = 3;

	while(attempts > 0)
	{
		System.out.println();
		System.out.println("================================");
		System.out.println("            FIND ROUTE");
		System.out.println("================================");

		System.out.print("Enter Source : ");
		String source = sc.nextLine();

		System.out.print("Enter Destination : ");
		String destination = sc.nextLine();

		String sourceResult = routeService.findStation(source);
		String destinationResult = routeService.findStation(destination);

		if(sourceResult == null)
		{
			System.out.println("Source Station Not Found.");
			attempts--;
			System.out.printf("You have %d Attempts remaining.%n", attempts);
			continue;
		}

		if(destinationResult == null)
		{
			System.out.println("Destination Station Not Found.");
			attempts--;
			System.out.printf("You have %d Attempts remaining.%n", attempts);
			continue;
		}

		String sourceLine = sourceResult.replaceAll("\\d", "");
		String destinationLine = destinationResult.replaceAll("\\d", "");
		int sourceIndex = Integer.parseInt(sourceResult.replaceAll("\\D", ""));
		int destinationIndex = Integer.parseInt(destinationResult.replaceAll("\\D", ""));

		String sourceStation = routeService.getStationName(sourceLine, sourceIndex);
		String destinationStation = routeService.getStationName(destinationLine, destinationIndex);

		System.out.println();
		System.out.println("Source : " + sourceStation);
		System.out.println("Destination : " + destinationStation);

		if(sourceStation.equals(destinationStation))
		{
			System.out.println();
			System.out.println("Don't provide same station names in SOURCE and DESTINATION.");
			attempts--;
			System.out.printf("You have %d Attempts remaining.%n", attempts);
			continue;
		}

		if(sourceLine.equals(destinationLine))
		{
			routeService.displayRoute(sourceStation, destinationStation, sourceLine);
			return;
		}

		String mix = sourceLine + destinationLine;
		String changeStation = routeService.getInterchangeStation(mix);

		if(changeStation == null)
		{
			System.out.println("Route not available.");
			return;
		}

		routeService.displayRoute(sourceStation, destinationStation, sourceLine, destinationLine, mix, changeStation);
		return;
	}

	System.out.println();
	System.out.println("Maximum attempts exceeded.");
}

	public static String normalizeStationName(String station)
	{
		station = station.trim();
		station = station.toLowerCase();
		station = station.replace(".", "");
		station = station.replace(",", "");
		station = station.replace("-", " ");
		station = station.replaceAll("\\s+", " ");
		station = station.replaceAll("\\s", "");
		return station;
	}

}
