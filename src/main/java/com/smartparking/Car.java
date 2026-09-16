package com.smartparking;

/** Represents a car. Charged at a higher hourly rate and needs a "LARGE" slot. */
public class Car extends Vehicle {

    private static final double HOURLY_RATE = 30.0;

    public Car(String vehicleNumber, String ownerName) {
        super(vehicleNumber, ownerName);
    }

    @Override
    public double getHourlyRate() {
        return HOURLY_RATE;
    }

    @Override
    public String getVehicleType() {
        return "CAR";
    }
}
