package com.smartparking;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * MODULE 2 (billing half): Calculates parking fare.
 * Kept separate from ParkingLot so the fare formula can change
 * (e.g. add discounts, night charges) without touching entry/exit logic.
 */
public class BillingService {

    private static final double MINIMUM_CHARGE = 10.0;

    /**
     * Fare = ceil(hours parked) * hourly rate for that vehicle type,
     * with a minimum charge so a 2-minute stay isn't free.
     */
    public double calculateFare(Ticket ticket, LocalDateTime exitTime) {
        Duration parkedDuration = Duration.between(ticket.getEntryTime(), exitTime);
        long minutes = Math.max(parkedDuration.toMinutes(), 1);
        long hoursBilled = (long) Math.ceil(minutes / 60.0);

        double rate = ticket.getVehicleType().equals("CAR") ? new Car("X", "X").getHourlyRate()
                                                             : new Bike("X", "X").getHourlyRate();
        double fare = hoursBilled * rate;
        return Math.max(fare, MINIMUM_CHARGE);
    }
}
