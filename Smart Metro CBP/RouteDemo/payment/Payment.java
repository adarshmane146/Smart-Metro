package payment;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Payment
{
	public static PaymentDetails paymentOptions(Scanner sc)
	{
		int tries = 3;

		while(tries > 0)
		{
			System.out.println();
			System.out.println("===============================================");
			System.out.println("\tPayment Options");
			System.out.println("===============================================");
			System.out.println("1. GooglePay");
			System.out.println("2. PhonePay");
			System.out.println("3. Slice");
			System.out.println("4. Back");
			System.out.println();
			System.out.print("Enter Choice : ");

			try
			{
				int choice = sc.nextInt();
				sc.nextLine();

				if(choice == 1)
				{
					System.out.println();
					System.out.println("GooglePay Selected");
					return makeUPIPayment("GooglePay", sc);
				}
				else if(choice == 2)
				{
					System.out.println();
					System.out.println("PhonePay Selected");
					return makeUPIPayment("PhonePay", sc);
				}
				else if(choice == 3)
				{
					System.out.println();
					System.out.println("Slice Selected");
					return makeUPIPayment("Slice", sc);
				}
				else if(choice == 4)
				{
					return null;
				}
				else
				{
					throw new IllegalArgumentException("Please choose between 1 and 4.");
				}
			}
			catch(InputMismatchException e)
			{
				System.out.println();
				System.out.println("Invalid Input. Please enter a number.");
				sc.nextLine();

				tries--;
				System.out.printf("You have %d attempts only.%n", tries);
			}
			catch(IllegalArgumentException e)
			{
				System.out.println();
				System.out.println(e.getMessage());

				tries--;
				System.out.printf("You have %d attempts only.%n", tries);
			}
		}

		System.out.println();
		System.out.println("Maximum attempts completed.");

		return null;
	}

	public static PaymentDetails makeUPIPayment(String paymentPlatform, Scanner sc)
	{
		int tries = 3;

		while(tries > 0)
		{
			try
			{
				System.out.println();
				System.out.println("-----------------------------------------------");
				System.out.println("\t" + paymentPlatform + " Payment");
				System.out.println("-----------------------------------------------");

				System.out.print("Enter UPI ID : ");
				String upi = sc.nextLine();

				if(upi.isBlank())
					throw new IllegalArgumentException("UPI ID cannot be empty.");

				if(!upi.matches("[a-zA-Z0-9._-]+@[a-zA-Z]+"))
					throw new IllegalArgumentException("Invalid UPI ID.");

				int otp = generateOTP();

				System.out.println();
				System.out.println("OTP Generated : " + otp);
				System.out.println("OTP has been sent to your registered mobile number.");

				int otpAttempts = 2;

				while(otpAttempts > 0)
				{
					try
					{
						System.out.print("Enter OTP : ");
						int enteredOtp = sc.nextInt();
						sc.nextLine();

						if(enteredOtp == otp)
						{
							String transactionId = generateTransactionId();

							System.out.println();
							System.out.println("Payment Successful.");
							System.out.println("Transaction ID : " + transactionId);

							return new PaymentDetails(paymentPlatform, transactionId);
						}
						else
						{
							otpAttempts--;

							System.out.println("Incorrect OTP.");

							if(otpAttempts > 0)
								System.out.println("You have 1 chance remaining.");
						}
					}
					catch(InputMismatchException e)
					{
						sc.nextLine();
						otpAttempts--;

						System.out.println("Please enter a valid OTP.");

						if(otpAttempts > 0)
							System.out.println("You have 1 chance remaining.");
					}
				}

				System.out.println();
				System.out.println("OTP verification failed.");
				System.out.println("Payment Failed.");

				return null;
			}
			catch(IllegalArgumentException e)
			{
				System.out.println();
				System.out.println(e.getMessage());

				tries--;

				System.out.printf("You have %d attempts only.%n", tries);
			}
		}

		System.out.println();
		System.out.println("Maximum attempts completed.");

		return null;
	}

	public static int generateOTP()
	{
		Random random = new Random();
		return 100000 + random.nextInt(900000);
	}

	public static String generateTransactionId()
	{
		Random random = new Random();
		int number = 100000 + random.nextInt(900000);
		return "TXN" + number;
	}
}
