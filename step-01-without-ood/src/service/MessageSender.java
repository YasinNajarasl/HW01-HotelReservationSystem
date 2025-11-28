package service;

public class MessageSender {
    
    // Original method
    public void sendEmailMessage(String message, String recipient) {
        System.out.println("📧 Email sent to " + recipient + ": " + message);
    }
    
    // ✅ NEW: Added SMS capability
    public void sendSmsMessage(String message, String recipient) {
        System.out.println("📱 SMS sent to " + recipient + ": " + message);
    }
}