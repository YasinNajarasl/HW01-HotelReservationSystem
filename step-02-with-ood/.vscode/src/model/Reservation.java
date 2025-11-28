package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Reservation {
    private Customer customer;
    private Room room;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDateTime bookingTime;
    private double totalAmount;
    private boolean isConfirmed;

    public Reservation(Customer customer, Room room, LocalDate checkInDate, LocalDate checkOutDate) {
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.bookingTime = LocalDateTime.now();
        this.totalAmount = calculateTotalAmount(room, checkInDate, checkOutDate);
        this.isConfirmed = false;
    }

    private double calculateTotalAmount(Room room, LocalDate checkIn, LocalDate checkOut) {
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        return nights * room.getPricePerNight();
    }

    // Getters
    public Customer getCustomer() { return customer; }
    public Room getRoom() { return room; }
    public LocalDate getCheckInDate() { return checkInDate; }
    public LocalDate getCheckOutDate() { return checkOutDate; }
    public LocalDateTime getBookingTime() { return bookingTime; }
    public double getTotalAmount() { return totalAmount; }
    public boolean isConfirmed() { return isConfirmed; }
    
    public void confirm() { this.isConfirmed = true; }
    
    public boolean overlapsWith(LocalDate start, LocalDate end) {
        return (checkInDate.isBefore(end) && checkOutDate.isAfter(start));
    }
    
    @Override
    public String toString() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return String.format("Reservation: %s | Room: %d | %s to %s | $%.2f",
            customer.getName(),
            room.getRoomNumber(),
            checkInDate.format(dateFormatter),
            checkOutDate.format(dateFormatter),
            totalAmount
        );
    }
}