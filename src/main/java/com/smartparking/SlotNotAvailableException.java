package com.smartparking;

/** Thrown when a vehicle tries to enter but no free slot exists. */
public class SlotNotAvailableException extends Exception {
    public SlotNotAvailableException(String message) {
        super(message);
    }
}
