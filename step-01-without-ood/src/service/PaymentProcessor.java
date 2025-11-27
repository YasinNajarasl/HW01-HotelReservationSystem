package service;

public class PaymentProcessor {
    
    public boolean processCreditCardPayment(double amount, String customerName) {
        System.out.println("💳 Credit card payment of $" + amount + " for " + customerName + " processed successfully");
        return true;
    }
}