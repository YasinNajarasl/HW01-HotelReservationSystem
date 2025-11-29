package interfaces;

import model.Customer;

public interface INotificationMethod extends INotificationSender {
    String getName();
    String getType();
    String getRecipient(Customer customer);
}