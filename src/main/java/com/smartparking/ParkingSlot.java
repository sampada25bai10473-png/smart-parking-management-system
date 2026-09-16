package com.smartparking;

/**
 * Represents one physical parking slot.
 * A slot is either free or occupied by exactly one vehicle at a time.
 */
public class ParkingSlot {

    private final int slotId;
    private boolean occupied;
    private Vehicle parkedVehicle;

    public ParkingSlot(int slotId) {
        this.slotId = slotId;
        this.occupied = false;
        this.parkedVehicle = null;
    }

    public int getSlotId() {
        return slotId;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public Vehicle getParkedVehicle() {
        return parkedVehicle;
    }

    public void occupy(Vehicle vehicle) {
        this.parkedVehicle = vehicle;
        this.occupied = true;
    }

    public void vacate() {
        this.parkedVehicle = null;
        this.occupied = false;
    }

    @Override
    public String toString() {
        return "Slot#" + slotId + " -> " + (occupied ? parkedVehicle.toString() : "EMPTY");
    }
}
