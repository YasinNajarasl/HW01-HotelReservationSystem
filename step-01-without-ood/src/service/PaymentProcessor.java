package service;

public class PaymentProcessor {
    
    // Original method
    public boolean processCreditCardPayment(double amount, String customerName) {
        System.out.println("💳 Credit card payment of $" + amount + " for " + customerName + " processed successfully");
        return true;
    }
    
    // ✅ NEW: Added On-site payment capability
    public boolean processOnSitePayment(double amount, String customerName) {
        System.out.println("🏢 On-site payment of $" + amount + " for " + customerName + " received successfully");
        return true;
    }
}