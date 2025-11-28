package service;

import interfaces.INotificationMethod;
import interfaces.IPaymentMethod;
import model.Customer;
import model.Reservation;
import model.Room;

import java.time.LocalDate;

public class ReservationService {
    private final Room[] availableRooms;

    public ReservationService() {
        this.availableRooms = new Room[] {
            new Room(101, "Luxury", 250.0),
            new Room(102, "Deluxe", 180.0),
            new Room(103, "Standard", 120.0),
            new Room(201, "Luxury", 260.0),
            new Room(202, "Deluxe", 190.0),
            new Room(203, "Standard", 130.0)
        };
    }

    /**
     * ✅ ULTRA TRANSPARENT: فقط interface ها!
     * ❌ بدون string, if-else, factory
     */
    public Reservation makeReservation(Customer customer, int roomNumber, 
                                     LocalDate checkInDate, LocalDate checkOutDate,
                                     IPaymentMethod paymentMethod, 
                                     INotificationMethod notificationMethod) {
        
        // اعتبارسنجی تاریخ
        if (!isValidDateRange(checkInDate, checkOutDate)) {
            System.out.println("❌ Invalid date range: Check-out must be after check-in");
            return null;
        }

        // پیدا کردن اتاق
        Room room = findRoomByNumber(roomNumber);
        if (room == null) {
            System.out.println("❌ Room " + roomNumber + " not found");
            return null;
        }

        // بررسی در دسترس بودن
        if (!room.isAvailable(checkInDate, checkOutDate)) {
            System.out.println("❌ Room " + roomNumber + " is not available for the selected dates");
            System.out.println("   Existing reservations:");
            printRoomReservations(room);
            return null;
        }

        // ایجاد رزرو
        Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);
        room.addReservation(reservation);

        // ✅ SUPER SIMPLE PAYMENT!
        boolean paymentSuccess = paymentMethod.processPayment(
            reservation.getTotalAmount(), customer.getName());

        if (paymentSuccess) {
            reservation.confirm();
            
            // پیام تأیید
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

            // ✅ ULTRA TRANSPARENT NOTIFICATION!
            // ✨ همه چیز خودکار از interface!
            notificationMethod.sendMessage(confirmationMessage, 
                notificationMethod.getRecipient(customer));
            
            System.out.println("🎉 Reservation completed successfully!");
            return reservation;
        } else {
            System.out.println("❌ Payment failed!");
            room.getReservations().remove(reservation);
            return null;
        }
    }

    public Room[] getAvailableRooms() {
        return availableRooms.clone();
    }

    // متدهای کمکی (بدون تغییر)
    private Room findRoomByNumber(int roomNumber) {
        for (Room room : availableRooms) {
            if (room.getRoomNumber() == roomNumber) return room;
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