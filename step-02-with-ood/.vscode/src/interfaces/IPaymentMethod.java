package interfaces;

public interface IPaymentMethod extends IPaymentProcessor {
    String getName();
    String getType();
}