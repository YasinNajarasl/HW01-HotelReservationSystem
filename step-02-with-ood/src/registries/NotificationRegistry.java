package registries;

import interfaces.INotificationMethod;

import java.util.ArrayList;
import java.util.List;

public class NotificationRegistry {
    private static final List<INotificationMethod> methods = new ArrayList<>();
    
    public static void register(INotificationMethod method) {
        methods.add(method);
    }
    
    // ✅ Static registration
    static {
        register(new implementations.EmailNotification());
        register(new implementations.SmsNotification()); // adding new feature by just adding one line the regestry class
    }
    
    public static List<INotificationMethod> getAll() {
        return new ArrayList<>(methods);
    }
    
    public static INotificationMethod findByType(String type) {
        return methods.stream()
            .filter(m -> m.getType().equalsIgnoreCase(type))
            .findFirst()
            .orElse(methods.get(0));
    }
}