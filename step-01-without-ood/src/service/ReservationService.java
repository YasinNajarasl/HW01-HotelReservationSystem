package service;

import model.Customer;
import model.Reservation;
import model.Room;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class ReservationService {
    private MessageSender messageSender;
    private PaymentProcessor paymentProcessor;
    private Room[] availableRooms;

    public ReservationService() {
        this.messageSender = new MessageSender();
        this.paymentProcessor = new PaymentProcessor();
        
        this.availableRooms = new Room[] {
            new Room(101, "Luxury", 250.0),
            new Room(102, "Deluxe", 180.0),
            new Room(103, "Standard", 120.0),
            new Room(201, "Luxury", 260.0),
            new Room(202, "Deluxe", 190.0),
            new Room(203, "Standard", 130.0)
        };
    }

    public Reservation makeReservation(Customer customer, int roomNumber, 
                                     LocalDate checkInDate, LocalDate checkOutDate, 
                                     String paymentType, String notificationType) {  // ✅ NEW: Added notificationType parameter
        
        if (!isValidDateRange(checkInDate, checkOutDate)) {
            System.out.println("❌ Invalid date range: Check-out must be after check-in");
            return null;
        }

        Room room = findRoomByNumber(roomNumber);
        if (room == null) {
            System.out.println("❌ Room " + roomNumber + " not found");
            return null;
        }

        if (!room.isAvailable(checkInDate, checkOutDate)) {
            System.out.println("❌ Room " + roomNumber + " is not available for the selected dates");
            System.out.println("   Existing reservations:");
            printRoomReservations(room);
            return null;
        }

        Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);
        room.addReservation(reservation);

        // ✅ CHANGE 1: Enhanced payment processing with On-site support
        boolean paymentSuccess = false;
        if ("credit".equalsIgnoreCase(paymentType)) {
            paymentSuccess = paymentProcessor.processCreditCardPayment(
                reservation.getTotalAmount(), customer.getName());
        } 
        // ✅ NEW: Added On-site payment condition
        else if ("onsite".equalsIgnoreCase(paymentType)) {
            paymentSuccess = paymentProcessor.processOnSitePayment(
                reservation.getTotalAmount(), customer.getName());
        } 
        // ✅ NEW: Added else clause for invalid payment types
        else {
            System.out.println("❌ Invalid payment type: " + paymentType);
            room.getReservations().remove(reservation);
            return null;
        }

        if (paymentSuccess) {
            reservation.confirm();
            
            String confirmationMessage = String.format(
                "✅ Reservation confirmed for %s\n" +
                "Room: %d (%s)\n" +
                "Check-in: %s\n" +
                "Check-out: %s\n" +
                "Total: $%.2f\n" +
                "Booking Time: %s",
                customer.getName(), 
                room.getRoomNumber(),
                room.getRoomType(),
                reservation.getCheckInDate(),
                reservation.getCheckOutDate(),
                reservation.getTotalAmount(),
                reservation.getBookingTime()
            );

            // ✅ CHANGE 2: Enhanced notification with SMS support
            if ("sms".equalsIgnoreCase(notificationType)) {
                // ✅ NEW: SMS notification
                messageSender.sendSmsMessage(confirmationMessage, customer.getPhoneNumber());
            } else {
                // Default: Email notification
                messageSender.sendEmailMessage(confirmationMessage, customer.getEmail());
            }
            
            System.out.println("🎉 Reservation completed successfully!");
            return reservation;
        } else {
            System.out.println("❌ Payment failed!");
            room.getReservations().remove(reservation);
            return null;
        }
    }

    // Other methods remain unchanged...
    public Room[] getAvailableRooms() {
        return availableRooms.clone();
    }

    private Room findRoomByNumber(int roomNumber) {
        for (Room room : availableRooms) {
            if (room.getRoomNumber() == roomNumber) {
                return room;
            }
        }
        return null;
    }

    private boolean isValidDateRange(LocalDate checkIn, LocalDate checkOut) {
        return checkOut.isAfter(checkIn);
    }

    private void printRoomReservations(Room room) {
        for (Reservation res : room.getReservations()) {
            if (res.isConfirmed()) {
                System.out.println("   - " + res);
            }
        }
    }
}