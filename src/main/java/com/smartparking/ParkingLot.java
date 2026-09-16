package com.smartparking;

import java.time.LocalDateTime;
import java.util.*;

/**
 * MODULE 1 & 2: Vehicle Entry/Exit + Slot Allocation.
 *
 * Uses a HashMap for O(1) slot lookup by id, and keeps active tickets
 * indexed by vehicle number so exit lookups are also O(1) on average.
 */
public class ParkingLot {

    private final Map<Integer, ParkingSlot> slots;
    private final Map<String, Ticket> activeTicketsByVehicle;
    private final List<Ticket> allTickets;
    private final FileManager fileManager;
    private int ticketSequence;

    public ParkingLot(int totalSlots, FileManager fileManager) {
        this.slots = new HashMap<>();
        for (int i = 1; i <= totalSlots; i++) {
            slots.put(i, new ParkingSlot(i));
        }
        this.activeTicketsByVehicle = new HashMap<>();
        this.fileManager = fileManager;
        this.allTickets = new ArrayList<>(fileManager.loadTickets());
        this.ticketSequence = allTickets.size();

        // Rebuild slot occupancy state from tickets that were never closed.
        for (Ticket t : allTickets) {
            if (t.isActive()) {
                ParkingSlot slot = slots.get(t.getSlotId());
                if (slot != null) {
                    Vehicle placeholder = t.getVehicleType().equals("CAR")
                            ? new Car(t.getVehicleNumber(), "UNKNOWN")
                            : new Bike(t.getVehicleNumber(), "UNKNOWN");
                    slot.occupy(placeholder);
                }
                activeTicketsByVehicle.put(t.getVehicleNumber(), t);
            }
        }
    }

    /** Registers a vehicle entry: validates, finds a free slot, issues a ticket. */
    public Ticket vehicleEntry(Vehicle vehicle) throws SlotNotAvailableException, InvalidVehicleException {
        if (vehicle == null) {
            throw new InvalidVehicleException("Vehicle details cannot be null");
        }
        if (activeTicketsByVehicle.containsKey(vehicle.getVehicleNumber())) {
            throw new InvalidVehicleException(
                    "Vehicle " + vehicle.getVehicleNumber() + " is already parked inside");
        }

        ParkingSlot freeSlot = findFreeSlot();
        if (freeSlot == null) {
            throw new SlotNotAvailableException("Parking full: no free slot for " + vehicle.getVehicleType());
        }

        freeSlot.occupy(vehicle);
        ticketSequence++;
        String ticketId = "TKT" + String.format("%04d", ticketSequence);
        Ticket ticket = new Ticket(ticketId, vehicle.getVehicleNumber(),
                vehicle.getVehicleType(), freeSlot.getSlotId(), LocalDateTime.now());

        allTickets.add(ticket);
        activeTicketsByVehicle.put(vehicle.getVehicleNumber(), ticket);
        fileManager.saveAllTickets(allTickets);
        fileManager.log("ENTRY: " + ticket.toString());

        return ticket;
    }

    /** Registers a vehicle exit and returns the closed ticket (billing is done by BillingService). */
    public Ticket vehicleExit(String vehicleNumber, BillingService billingService) throws InvalidVehicleException {
        String key = vehicleNumber.trim().toUpperCase();
        Ticket ticket = activeTicketsByVehicle.get(key);
        if (ticket == null) {
            throw new InvalidVehicleException("No active parking record found for vehicle " + key);
        }

        LocalDateTime exitTime = LocalDateTime.now();
        double amount = billingService.calculateFare(ticket, exitTime);
        ticket.closeTicket(exitTime, amount);

        ParkingSlot slot = slots.get(ticket.getSlotId());
        if (slot != null) {
            slot.vacate();
        }
        activeTicketsByVehicle.remove(key);
        fileManager.saveAllTickets(allTickets);
        fileManager.log("EXIT: " + ticket.toString());

        return ticket;
    }

    private ParkingSlot findFreeSlot() {
        for (ParkingSlot slot : slots.values()) {
            if (!slot.isOccupied()) {
                return slot;
            }
        }
        return null;
    }

    public List<ParkingSlot> getAllSlots() {
        List<ParkingSlot> list = new ArrayList<>(slots.values());
        list.sort(Comparator.comparingInt(ParkingSlot::getSlotId));
        return list;
    }

    public List<Ticket> getAllTickets() {
        return Collections.unmodifiableList(allTickets);
    }

    public long getOccupiedCount() {
        return slots.values().stream().filter(ParkingSlot::isOccupied).count();
    }

    public int getTotalSlots() {
        return slots.size();
    }
}
