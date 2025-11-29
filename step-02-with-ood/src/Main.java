import interfaces.INotificationMethod;
import interfaces.IPaymentMethod;
import model.Customer;
import model.Reservation;
import model.Room;
import registries.NotificationRegistry;
import registries.PaymentRegistry;
import service.ReservationService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final ReservationService reservationService = new ReservationService();
    
    public static void main(String[] args) {
        System.out.println("=====================================");
        System.out.println("      Hotel Reservation System       ");
        System.out.println("   🚀 Self-Describing Registry       ");
        System.out.println("      SOLID 100% Compliant           ");
        System.out.println("=====================================");
        
        while (true) {
            showDynamicMainMenu();
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    makeReservation();
                    break;
                case 2:
                    showAvailableRooms();
                    break;
                case 3:
                    System.out.println("👋 Thank you for using our system!");
                    scanner.close();
                    return;
                default:
                    System.out.println("❌ Invalid choice! Please try again.");
            }
            
            System.out.println("\n" + "=".repeat(60) + "\n");
        }
    }
    
    private static void showDynamicMainMenu() {
        System.out.println("\n📋 Main Menu:");
        System.out.println("1️⃣  Make Reservation");
        System.out.println("2️⃣  View Available Rooms");
        System.out.println("3️⃣  Exit");
        
    
        System.out.print("\n➤ ");
    }
    
    private static void makeReservation() {
        System.out.println("\n🏨 Make New Reservation");
        System.out.println("-".repeat(50));
        
        // ایجاد مشتری
        Customer customer = createCustomer();
        if (customer == null) {
            System.out.println("❌ Customer creation cancelled.");
            return;
        }
        
        // دریافت تاریخ‌ها
        LocalDate checkInDate = getDateInput("Enter check-in date (yyyy-MM-dd): ");
        if (checkInDate == null) return;
        
        LocalDate checkOutDate = getDateInput("Enter check-out date (yyyy-MM-dd): ");
        if (checkOutDate == null) return;
        
        // نمایش اتاق‌های در دسترس
        Room[] rooms = reservationService.getAvailableRooms();
        showRoomsWithAvailability(rooms, checkInDate, checkOutDate);
        
        int roomNumber = getIntInput("Select room number: ");
        
        // ✅ DYNAMIC SELECTION از Registry
        IPaymentMethod paymentMethod = selectPaymentMethod();
        INotificationMethod notificationMethod = selectNotificationMethod();
        
        System.out.println("\n🔄 Processing reservation...");
        System.out.println("-".repeat(40));
        
        // ✅ CALL ساده و شفاف!
        Reservation reservation = reservationService.makeReservation(
            customer, roomNumber, checkInDate, checkOutDate, 
            paymentMethod, notificationMethod
        );
        
        if (reservation != null && reservation.isConfirmed()) {
            System.out.println("\n✅ Reservation completed successfully!");
            printReservationDetails(reservation);
            System.out.printf("💳 Payment Method: %s%n", paymentMethod.getName());
            System.out.printf("📬 Notification: %s%n", notificationMethod.getName());
        }
    }
    
    private static IPaymentMethod selectPaymentMethod() {
        List<IPaymentMethod> methods = PaymentRegistry.getAll();
        System.out.println("\n💳 Select Payment Method:");
        System.out.println("-".repeat(40));
        
        for (int i = 0; i < methods.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, methods.get(i).getName());
        }
        
        int choice = getIntInput("Select payment method (1-" + methods.size() + "): ") - 1;
        choice = Math.max(0, Math.min(choice, methods.size() - 1));
        return methods.get(choice);
    }
    
    private static INotificationMethod selectNotificationMethod() {
        List<INotificationMethod> methods = NotificationRegistry.getAll();
        System.out.println("\n📬 Select Notification Method:");
        System.out.println("-".repeat(45));
        
        for (int i = 0; i < methods.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, methods.get(i).getName());
        }
        
        int choice = getIntInput("Select notification method (1-" + methods.size() + "): ") - 1;
        choice = Math.max(0, Math.min(choice, methods.size() - 1));
        return methods.get(choice);
    }
    
    private static void showAvailableRooms() {
        System.out.println("\n🛏️  All Available Rooms:");
        System.out.println("-".repeat(50));
        
        Room[] rooms = reservationService.getAvailableRooms();
        System.out.println("Room No | Type        | Price/night");
        System.out.println("-".repeat(50));
        
        for (Room room : rooms) {
            System.out.printf("%7d | %-11s | $%10.2f%n",
                room.getRoomNumber(),
                room.getRoomType(),
                room.getPricePerNight()
            );
        }
    }
    
    private static void showRoomsWithAvailability(Room[] rooms, LocalDate checkIn, LocalDate checkOut) {
        System.out.println("\n🛏️  Available Rooms for " + checkIn + " → " + checkOut + ":");
        System.out.println("-".repeat(80));
        System.out.println("Room No | Type        | Price/night | Available");
        System.out.println("-".repeat(80));
        
        for (Room room : rooms) {
            boolean available = room.isAvailable(checkIn, checkOut);
            System.out.printf("%7d | %-11s | $%10.2f | %s%n",
                room.getRoomNumber(),
                room.getRoomType(),
                room.getPricePerNight(),
                available ? "✅ YES" : "❌ NO"
            );
        }
        System.out.println("-".repeat(80));
    }
    
    private static Customer createCustomer() {
        System.out.println("\n👤 Customer Information:");
        System.out.println("-".repeat(30));
        
        System.out.print("Enter customer name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) return null;
        
        System.out.print("Enter customer email: ");
        String email = scanner.nextLine().trim();
        if (email.isEmpty()) return null;
        
        System.out.print("Enter customer phone number: ");
        String phone = scanner.nextLine().trim();
        if (phone.isEmpty()) return null;
        
        return new Customer(name, email, phone);
    }
    
    private static void printReservationDetails(Reservation reservation) {
        System.out.println("📄 Reservation Details:");
        System.out.println("-".repeat(40));
        System.out.printf("   👤 Customer: %s%n", reservation.getCustomer().getName());
        System.out.printf("   🛏️  Room: %d (%s)%n", 
            reservation.getRoom().getRoomNumber(), 
            reservation.getRoom().getRoomType());
        System.out.printf("   📅 Check-in: %s%n", reservation.getCheckInDate());
        System.out.printf("   📅 Check-out: %s%n", reservation.getCheckOutDate());
        System.out.printf("   💰 Total: $%.2f%n", reservation.getTotalAmount());
        System.out.printf("   ⏰ Booking Time: %s%n", reservation.getBookingTime());
        System.out.println("-".repeat(40));
    }
    
    // متدهای کمکی برای ورودی
    private static LocalDate getDateInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) return null;
                return LocalDate.parse(input, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("❌ Invalid date format! Use yyyy-MM-dd (e.g., 2024-12-01)");
            }
        }
    }
    
    private static int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    System.out.println("❌ Input cannot be empty!");
                    continue;
                }
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number!");
            }
        }
    }
}