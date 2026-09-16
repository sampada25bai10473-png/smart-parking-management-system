package com.smartparking;

/** Represents a two-wheeler. Cheaper hourly rate and needs a "SMALL" slot. */
public class Bike extends Vehicle {

    private static final double HOURLY_RATE = 10.0;

    public Bike(String vehicleNumber, String ownerName) {
        super(vehicleNumber, ownerName);
    }

    @Override
    public double getHourlyRate() {
        return HOURLY_RATE;
    }

    @Override
    public String getVehicleType() {
        return "BIKE";
    }
}
