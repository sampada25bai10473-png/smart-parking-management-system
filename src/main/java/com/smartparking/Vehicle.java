package com.smartparking;

/**
 * Abstract base class representing a generic vehicle.
 * Concrete vehicle types (Car, Bike) extend this class and
 * provide their own hourly rate, demonstrating runtime polymorphism.
 */
public abstract class Vehicle {

    private final String vehicleNumber;
    private final String ownerName;

    public Vehicle(String vehicleNumber, String ownerName) {
        if (vehicleNumber == null || vehicleNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Vehicle number cannot be empty");
        }
        this.vehicleNumber = vehicleNumber.trim().toUpperCase();
        this.ownerName = ownerName;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    /** Every vehicle type defines its own per-hour parking charge. */
    public abstract double getHourlyRate();

    /** Every vehicle type defines the slot category it needs. */
    public abstract String getVehicleType();

    @Override
    public String toString() {
        return getVehicleType() + " [" + vehicleNumber + "] owned by " + ownerName;
    }
}
