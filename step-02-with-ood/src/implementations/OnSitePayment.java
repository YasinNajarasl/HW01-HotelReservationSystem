package implementations;

import interfaces.IPaymentMethod;

//  new class implementing new payment method
public class OnSitePayment implements IPaymentMethod {
    @Override
    public boolean processPayment(double amount, String customerName) {
        System.out.println("🏢 On-site payment of $" + amount + " for " + customerName + " received successfully");
        return true;
    }

    @Override
    public String getName() {
        return "On-site Payment";
    }

    @Override
    public String getType() {
        return "onsite";
    }
}