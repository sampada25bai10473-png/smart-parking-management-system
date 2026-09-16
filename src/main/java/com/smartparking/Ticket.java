package com.smartparking;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a parking ticket issued at entry and closed at exit.
 * Holds all the information needed for billing and reporting.
 */
public class Ticket {

    private static final DateTimeFormatter FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String ticketId;
    private final String vehicleNumber;
    private final String vehicleType;
    private final int slotId;
    private final LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private double amountPaid;

    public Ticket(String ticketId, String vehicleNumber, String vehicleType,
                   int slotId, LocalDateTime entryTime) {
        this.ticketId = ticketId;
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        this.slotId = slotId;
        this.entryTime = entryTime;
        this.exitTime = null;
        this.amountPaid = 0.0;
    }

    public String getTicketId() {
        return ticketId;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public int getSlotId() {
        return slotId;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public void closeTicket(LocalDateTime exitTime, double amountPaid) {
        this.exitTime = exitTime;
        this.amountPaid = amountPaid;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public boolean isActive() {
        return exitTime == null;
    }

    /** Serializes the ticket as one pipe-delimited line for file storage. */
    public String toFileRecord() {
        return String.join("|",
                ticketId,
                vehicleNumber,
                vehicleType,
                String.valueOf(slotId),
                entryTime.format(FORMAT),
                exitTime == null ? "-" : exitTime.format(FORMAT),
                String.valueOf(amountPaid));
    }

    /** Rebuilds a Ticket object from a stored file record. */
    public static Ticket fromFileRecord(String line) {
        String[] parts = line.split("\\|", -1);
        Ticket t = new Ticket(parts[0], parts[1], parts[2],
                Integer.parseInt(parts[3]),
                LocalDateTime.parse(parts[4], FORMAT));
        if (!parts[5].equals("-")) {
            t.exitTime = LocalDateTime.parse(parts[5], FORMAT);
            t.amountPaid = Double.parseDouble(parts[6]);
        }
        return t;
    }

    @Override
    public String toString() {
        return "Ticket " + ticketId + " | " + vehicleType + " " + vehicleNumber
                + " | Slot " + slotId + " | In: " + entryTime.format(FORMAT)
                + " | Out: " + (exitTime == null ? "STILL PARKED" : exitTime.format(FORMAT))
                + " | Paid: Rs." + amountPaid;
    }
}
