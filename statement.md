# Problem Statement

## Problem Statement

Parking facilities that rely on manual registers or verbal tracking struggle
with three recurring problems: knowing which slots are free in real time,
tracking how long each vehicle has been parked, and calculating a fair,
consistent parking fee at exit. Manual processes are slow, error-prone, and
leave no audit trail. This project builds a lightweight, terminal-based
Smart Parking Management System that automates slot allocation, fare
calculation, and reporting for a small-to-medium parking facility, without
requiring any external database or GUI setup.

## Scope of the Project

The system covers a single parking facility with a fixed, configurable
number of slots. It handles:

- Registering a vehicle's entry and assigning it a free slot
- Registering a vehicle's exit, calculating the fare, and freeing the slot
- Persisting all ticket data to local files so history survives a restart
- Generating occupancy, active-vehicle, and revenue reports on demand

Out of scope: multi-location support, online/mobile booking, payment
gateway integration, and license-plate recognition (camera-based entry) —
these are noted as future enhancements.

## Target Users

- **Parking attendants / operators** who register vehicles as they arrive and leave
- **Facility managers/owners** who need daily occupancy and revenue reports to track utilization and income

## High-Level Features

1. **Vehicle Entry & Exit Management** — validated entry with duplicate/slot-full checks, and exit with automatic fare calculation
2. **Slot Allocation & Billing** — O(1) slot lookup, per-vehicle-type hourly billing with a minimum charge
3. **Reports & Analytics** — real-time occupancy percentage, list of currently parked vehicles, and total/type-wise revenue
