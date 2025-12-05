package implementations;

import interfaces.INotificationMethod;
import model.Customer;

// new class implementing new notification method
public class SmsNotification implements INotificationMethod {
    @Override
    public void sendMessage(String message, String recipient) {
        System.out.println("📱 SMS sent to " + recipient + ": " + message);
    }

    @Override
    public String getName() {
        return "SMS";
    }

    @Override
    public String getType() {
        return "sms";
    }

    @Override
    public String getRecipient(Customer customer) {
        return customer.getPhoneNumber();
    }
}