package com.mdababi.dronedelivery.exceptions;

public class LowBatteryException extends RuntimeException {

    public LowBatteryException(String message) {
        super(message);
    }
}
