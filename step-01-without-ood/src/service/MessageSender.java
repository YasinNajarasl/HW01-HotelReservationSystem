package service;

public class MessageSender {
    
    public void sendEmailMessage(String message, String recipient) {
        System.out.println("📧 Email sent to " + recipient + ": " + message);
    }
}