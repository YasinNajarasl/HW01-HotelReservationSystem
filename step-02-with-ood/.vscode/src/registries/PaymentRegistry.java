package registries;

import interfaces.IPaymentMethod;

import java.util.ArrayList;
import java.util.List;

public class PaymentRegistry {
    private static final List<IPaymentMethod> methods = new ArrayList<>();
    
    // ✅ فقط کلاس بده! همه چیز خودکار است
    public static void register(IPaymentMethod method) {
        methods.add(method);
    }
    
    // ✅ Static registration
    static {
        register(new implementations.CreditCardPayment());
    }
    
    public static List<IPaymentMethod> getAll() {
        return new ArrayList<>(methods);
    }
    
    public static IPaymentMethod findByType(String type) {
        return methods.stream()
            .filter(m -> m.getType().equalsIgnoreCase(type))
            .findFirst()
            .orElse(methods.get(0));
    }
}