package com.smartparking;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Entry point: menu-driven command-line interface tying together
 * the entry/exit module, billing module, and reporting module.
 */
public class Main {

    private static final int TOTAL_SLOTS = 10;

    public static void main(String[] args) {
        FileManager fileManager = new FileManager("data");
        ParkingLot parkingLot = new ParkingLot(TOTAL_SLOTS, fileManager);
        BillingService billingService = new BillingService();
        ReportGenerator reportGenerator = new ReportGenerator(parkingLot);
        Scanner scanner = new Scanner(System.in);

        System.out.println("=================================================");
        System.out.println(" SMART PARKING MANAGEMENT SYSTEM");
        System.out.println("=================================================");

        boolean running = true;
        while (running) {
            printMenu();
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number (1-6).");
                continue;
            }

            switch (choice) {
                case 1:
                    handleEntry(scanner, parkingLot);
                    break;
                case 2:
                    handleExit(scanner, parkingLot, billingService);
                    break;
                case 3:
                    reportGenerator.printOccupancyReport();
                    break;
                case 4:
                    reportGenerator.printActiveVehiclesReport();
                    break;
                case 5:
                    reportGenerator.printRevenueReport();
                    break;
                case 6:
                    running = false;
                    System.out.println("Exiting. Thank you!");
                    break;
                default:
                    System.out.println("Invalid choice. Please select 1-6.");
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n1. Vehicle Entry");
        System.out.println("2. Vehicle Exit");
        System.out.println("3. Occupancy Report");
        System.out.println("4. Currently Parked Vehicles");
        System.out.println("5. Revenue Report");
        System.out.println("6. Exit Application");
        System.out.print("Choose an option: ");
    }

    private static void handleEntry(Scanner scanner, ParkingLot parkingLot) {
        try {
            System.out.print("Vehicle type (CAR/BIKE): ");
            String type = scanner.nextLine().trim().toUpperCase();
            System.out.print("Vehicle number: ");
            String number = scanner.nextLine().trim();
            System.out.print("Owner name: ");
            String owner = scanner.nextLine().trim();

            Vehicle vehicle;
            if (type.equals("CAR")) {
                vehicle = new Car(number, owner);
            } else if (type.equals("BIKE")) {
                vehicle = new Bike(number, owner);
            } else {
                throw new InvalidVehicleException("Unknown vehicle type: " + type + " (use CAR or BIKE)");
            }

            Ticket ticket = parkingLot.vehicleEntry(vehicle);
            System.out.println("Entry successful. " + ticket);

        } catch (SlotNotAvailableException | InvalidVehicleException | IllegalArgumentException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private static void handleExit(Scanner scanner, ParkingLot parkingLot, BillingService billingService) {
        try {
            System.out.print("Vehicle number: ");
            String number = scanner.nextLine().trim();
            Ticket closed = parkingLot.vehicleExit(number, billingService);
            System.out.println("Exit successful. " + closed);
        } catch (InvalidVehicleException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}
