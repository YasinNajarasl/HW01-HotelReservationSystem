import model.Customer;
import model.Reservation;
import model.Room;
import service.ReservationService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public static void main(String[] args) {
        System.out.println("=====================================");
        System.out.println("      Hotel Reservation System       ");
        System.out.println("=====================================");
        
        ReservationService reservationService = new ReservationService();
        
        while (true) {
            showMainMenu();
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    makeReservation(reservationService);
                    break;
                case 2:
                    showAvailableRooms(reservationService);
                    break;
                case 3:
                    System.out.println("👋 Thank you for using our system!");
                    return;
                default:
                    System.out.println("❌ Invalid choice! Please try again.");
            }
            
            System.out.println("\n" + "=".repeat(50) + "\n");
        }
    }
    
    private static void showMainMenu() {
        System.out.println("\n📋 Main Menu:");
        System.out.println("1️⃣  Make Reservation");
        System.out.println("2️⃣  View Available Rooms");
        System.out.println("3️⃣  Exit");
    }
    
    private static void makeReservation(ReservationService service) {
        System.out.println("\n🏨 Make New Reservation");
        System.out.println("-".repeat(35));
        
        // Create customer
        Customer customer = createCustomer();
        if (customer == null) {
            System.out.println("❌ Customer creation cancelled.");
            return;
        }
        
        // Get dates
        LocalDate checkInDate = getDateInput("Enter check-in date (yyyy-MM-dd): ");
        if (checkInDate == null) return;
        
        LocalDate checkOutDate = getDateInput("Enter check-out date (yyyy-MM-dd): ");
        if (checkOutDate == null) return;
        
        // Select room
        Room[] rooms = service.getAvailableRooms();
        showRoomsWithAvailability(rooms, checkInDate, checkOutDate);
        
        int roomNumber = getIntInput("Select room number: ");
        
        // Payment type
        String paymentType = selectPaymentType();
        
        // Make reservation
        System.out.println("\n🔄 Processing reservation...");
        Reservation reservation = service.makeReservation(
            customer, roomNumber, checkInDate, checkOutDate, paymentType
        );
        
        if (reservation != null && reservation.isConfirmed()) {
            System.out.println("\n✅ Reservation completed successfully!");
            printReservationDetails(reservation);
        }
    }
    
    private static void showRoomsWithAvailability(Room[] rooms, LocalDate checkIn, LocalDate checkOut) {
        System.out.println("\n🛏️  Available Rooms for " + checkIn + " to " + checkOut + ":");
        System.out.println("-".repeat(60));
        System.out.println("Room No | Type     | Price/night | Available");
        System.out.println("-".repeat(60));
        
        for (Room room : rooms) {
            boolean available = room.isAvailable(checkIn, checkOut);
            System.out.printf("%7d | %-8s | $%8.2f | %s%n",
                room.getRoomNumber(),
                room.getRoomType(),
                room.getPricePerNight(),
                available ? "✅ YES" : "❌ NO"
            );
        }
    }
    
    private static void showAvailableRooms(ReservationService service) {
        System.out.println("\n🛏️  All Rooms:");
        System.out.println("-".repeat(35));
        
        Room[] rooms = service.getAvailableRooms();
        System.out.println("Room No | Type     | Price/night");
        System.out.println("-".repeat(30));
        
        for (Room room : rooms) {
            System.out.printf("%7d | %-8s | $%8.2f%n",
                room.getRoomNumber(),
                room.getRoomType(),
                room.getPricePerNight()
            );
        }
    }
    
    private static Customer createCustomer() {
        System.out.println("\n👤 Customer Information:");
        System.out.println("-".repeat(25));
        
        System.out.print("Enter customer name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("❌ Name cannot be empty!");
            return null;
        }
        
        System.out.print("Enter customer email: ");
        String email = scanner.nextLine().trim();
        if (email.isEmpty()) {
            System.out.println("❌ Email cannot be empty!");
            return null;
        }
        
        System.out.print("Enter customer phone number: ");
        String phone = scanner.nextLine().trim();
        if (phone.isEmpty()) {
            System.out.println("❌ Phone number cannot be empty!");
            return null;
        }
        
        return new Customer(name, email, phone);
    }
    
    private static LocalDate getDateInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = scanner.nextLine().trim();
                return LocalDate.parse(input, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("❌ Invalid date format! Use yyyy-MM-dd (e.g., 2024-12-01)");
            }
        }
    }
    
    private static String selectPaymentType() {
        System.out.println("\n💳 Payment Methods:");
        System.out.println("-".repeat(20));
        System.out.println("1. Credit Card (Only available method)");
        System.out.println("Selected: Credit Card");
        return "credit";
    }
    
    private static void printReservationDetails(Reservation reservation) {
        System.out.println("📄 Reservation Details:");
        System.out.println("-".repeat(25));
        System.out.printf("   Customer: %s%n", reservation.getCustomer().getName());
        System.out.printf("   Room: %d (%s)%n", 
            reservation.getRoom().getRoomNumber(), 
            reservation.getRoom().getRoomType());
        System.out.printf("   Check-in: %s%n", reservation.getCheckInDate());
        System.out.printf("   Check-out: %s%n", reservation.getCheckOutDate());
        System.out.printf("   Total: $%.2f%n", reservation.getTotalAmount());
        System.out.printf("   Booking Time: %s%n", reservation.getBookingTime());
    }
    
    private static int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number!");
            }
        }
    }
}