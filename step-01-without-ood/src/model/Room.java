package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Room {
    private int roomNumber;
    private String roomType;
    private double pricePerNight;
    private List<Reservation> reservations;

    public Room(int roomNumber, String roomType, double pricePerNight) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.reservations = new ArrayList<>();
    }

    public boolean isAvailable(LocalDate checkInDate, LocalDate checkOutDate) {
        for (Reservation reservation : reservations) {
            if (reservation.isConfirmed() && 
                reservation.overlapsWith(checkInDate, checkOutDate)) {
                return false;
            }
        }
        return true;
    }

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    public List<Reservation> getReservations() {
        return new ArrayList<>(reservations);
    }

    // Getters
    public int getRoomNumber() { return roomNumber; }
    public String getRoomType() { return roomType; }
    public double getPricePerNight() { return pricePerNight; }
}