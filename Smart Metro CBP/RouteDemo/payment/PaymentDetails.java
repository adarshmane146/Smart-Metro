package payment;

public class PaymentDetails
{
	private String paymentMethod;
	private String transactionId;

	public PaymentDetails(String paymentMethod, String transactionId)
	{
		this.paymentMethod = paymentMethod;
		this.transactionId = transactionId;
	}

	public String getPaymentMethod()
	{
		return paymentMethod;
	}

	public String getTransactionId()
	{
		return transactionId;
	}
}
