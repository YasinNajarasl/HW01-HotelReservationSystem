package interfaces;

public interface IPaymentProcessor {
    boolean processPayment(double amount, String customerName);
}