package com.smartparking;

import java.util.List;

/**
 * MODULE 3: Reports & Analytics.
 * Summarizes revenue, occupancy, and vehicle-type breakdown from ticket history.
 */
public class ReportGenerator {

    private final ParkingLot parkingLot;

    public ReportGenerator(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }

    public void printOccupancyReport() {
        System.out.println("\n--- OCCUPANCY REPORT ---");
        long occupied = parkingLot.getOccupiedCount();
        int total = parkingLot.getTotalSlots();
        System.out.printf("Occupied: %d / %d  (%.1f%% full)%n",
                occupied, total, total == 0 ? 0 : (occupied * 100.0 / total));
        for (ParkingSlot slot : parkingLot.getAllSlots()) {
            System.out.println("  " + slot);
        }
    }

    public void printRevenueReport() {
        System.out.println("\n--- REVENUE REPORT ---");
        List<Ticket> tickets = parkingLot.getAllTickets();
        double totalRevenue = 0;
        int carCount = 0, bikeCount = 0;
        double carRevenue = 0, bikeRevenue = 0;

        for (Ticket t : tickets) {
            if (!t.isActive()) {
                totalRevenue += t.getAmountPaid();
                if (t.getVehicleType().equals("CAR")) {
                    carCount++;
                    carRevenue += t.getAmountPaid();
                } else {
                    bikeCount++;
                    bikeRevenue += t.getAmountPaid();
                }
            }
        }

        System.out.printf("Total completed visits : %d%n", carCount + bikeCount);
        System.out.printf("  Cars  : %d visits, Rs.%.2f revenue%n", carCount, carRevenue);
        System.out.printf("  Bikes : %d visits, Rs.%.2f revenue%n", bikeCount, bikeRevenue);
        System.out.printf("TOTAL REVENUE COLLECTED : Rs.%.2f%n", totalRevenue);
    }

    public void printActiveVehiclesReport() {
        System.out.println("\n--- CURRENTLY PARKED VEHICLES ---");
        boolean any = false;
        for (Ticket t : parkingLot.getAllTickets()) {
            if (t.isActive()) {
                System.out.println("  " + t);
                any = true;
            }
        }
        if (!any) {
            System.out.println("  (none)");
        }
    }
}
