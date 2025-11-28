package implementations;

import interfaces.IPaymentMethod;

public class CreditCardPayment implements IPaymentMethod {
    @Override
    public boolean processPayment(double amount, String customerName) {
        System.out.println("💳 Credit card payment of $" + amount + " for " + customerName + " processed successfully");
        return true;
    }

    @Override
    public String getName() {
        return "Credit Card";
    }

    @Override
    public String getType() {
        return "credit";
    }
}