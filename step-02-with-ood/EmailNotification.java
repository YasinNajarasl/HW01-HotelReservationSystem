package implementations;

import interfaces.INotificationMethod;
import model.Customer;

public class EmailNotification implements INotificationMethod {
    @Override
    public void sendMessage(String message, String recipient) {
        System.out.println("📧 Email sent to " + recipient + ": " + message);
    }

    @Override
    public String getName() {
        return "Email";
    }

    @Override
    public String getType() {
        return "email";
    }

    @Override
    public String getRecipient(Customer customer) {
        return customer.getEmail();
    }
}