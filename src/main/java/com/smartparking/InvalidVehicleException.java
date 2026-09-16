package com.smartparking;

/** Thrown when vehicle input data (number/type) is invalid. */
public class InvalidVehicleException extends Exception {
    public InvalidVehicleException(String message) {
        super(message);
    }
}
