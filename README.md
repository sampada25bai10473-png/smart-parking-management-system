# Smart Parking Management System

A command-line Java application that manages vehicle entry/exit, slot
allocation, automatic fare billing, and occupancy/revenue reporting for a
parking facility. Built as a course project to demonstrate core Java and
object-oriented programming concepts.

## Overview

Manual parking registers make it hard to track which slots are free, how
long a vehicle has been parked, and how much it should be charged. This
project solves that with a simple, file-persisted system that:

- Allocates a free slot the moment a vehicle enters
- Calculates the exact fare (per vehicle type, per hour) when it exits
- Keeps a running log of every visit for reporting and audit

## Features

- **Vehicle Entry** — register a Car or Bike, auto-assign the next free slot, issue a ticket
- **Vehicle Exit** — close the ticket, compute the bill, free up the slot
- **Occupancy Report** — see how many slots are free/occupied, slot-by-slot
- **Currently Parked Vehicles** — list every vehicle still inside
- **Revenue Report** — total and per-vehicle-type revenue collected
- **Persistent storage** — all tickets survive a restart (`data/tickets.txt`)
- **Activity log** — every entry/exit is timestamped in `data/activity.log`
- **Input validation & custom exceptions** — invalid vehicle type, duplicate entry, parking-full, and exit-without-entry are all handled gracefully instead of crashing

## Technologies / Tools Used

- Java 17+ (no external libraries — pure Java SE)
- `java.time` for timestamps and duration-based billing
- `java.nio.file` for file-based persistence (no database required)
- Git for version control

## Project Structure

```
SmartParkingManagementSystem/
├── src/main/java/com/smartparking/
│   ├── Vehicle.java              # abstract base class
│   ├── Car.java                  # Vehicle subtype
│   ├── Bike.java                 # Vehicle subtype
│   ├── ParkingSlot.java          # single slot state
│   ├── Ticket.java               # entry/exit record + serialization
│   ├── SlotNotAvailableException.java
│   ├── InvalidVehicleException.java
│   ├── FileManager.java          # all file I/O (persistence + logging)
│   ├── ParkingLot.java           # Module 1+2: entry/exit, slot allocation
│   ├── BillingService.java       # Module 2: fare calculation
│   ├── ReportGenerator.java      # Module 3: reports & analytics
│   └── Main.java                 # CLI menu, application entry point
├── data/                         # created automatically at runtime
│   ├── tickets.txt
│   └── activity.log
├── docs/                         # architecture & UML diagrams
├── statement.md
└── README.md
```

## Prerequisites

- **JDK 17 or later** installed on your machine
- Check with: `java -version` and `javac -version`
- If not installed: download from [Adoptium](https://adoptium.net/) or use your OS package manager (`sudo apt install openjdk-17-jdk` on Ubuntu/Debian)

## How to Set Up & Run

1. **Clone the repository**
   ```bash
   git clone https://github.com/<your-username>/<your-repo-name>.git
   cd <your-repo-name>
   ```

2. **Compile the project**
   ```bash
   javac -d bin src/main/java/com/smartparking/*.java
   ```
   This compiles all `.java` files into a `bin/` folder.

3. **Run the application**
   ```bash
   java -cp bin com.smartparking.Main
   ```

4. **Use the menu** that appears:
   ```
   1. Vehicle Entry
   2. Vehicle Exit
   3. Occupancy Report
   4. Currently Parked Vehicles
   5. Revenue Report
   6. Exit Application
   ```
   - Choose `1`, then enter vehicle type (`CAR` or `BIKE`), vehicle number, and owner name.
   - Choose `2` and enter the same vehicle number to exit and see the calculated bill.
   - Choose `3`, `4`, or `5` any time to see live reports.

5. **Data persistence** — a `data/` folder is created automatically in the directory you run the program from. Tickets are saved there and reloaded automatically the next time you start the app, so no data is lost between runs.

## Testing / Sample Run

No GUI or database setup is required — everything runs from the terminal.
A quick way to verify it works:

1. Run the app, choose `1`, enter `CAR`, `MP07AB1234`, `Rahul` → note the slot number printed.
2. Choose `3` to confirm that slot now shows as occupied.
3. Choose `2` and enter `MP07AB1234` → a fare is printed (minimum ₹10, since the visit is short).
4. Choose `5` to see the revenue report reflect that one completed visit.

## Screenshots

_Add terminal screenshots here after you run the program (menu, entry, exit with bill, reports)._

## Notes on Design Decisions

- **HashMap for slots** gives O(1) slot lookup instead of scanning a list.
- **File-based storage** (not a database) was chosen deliberately to keep the
  project runnable anywhere with zero external setup, per the "must run from
  command line with no extra services" requirement.
- **Abstract `Vehicle` class** with `Car`/`Bike` subclasses demonstrates
  inheritance and polymorphism — `getHourlyRate()` behaves differently per
  vehicle type without any `if/else` chains in the billing logic itself.
