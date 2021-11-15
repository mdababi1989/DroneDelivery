package com.mdababi.dronedelivery.exceptions;

public class DroneNotAvailableException extends RuntimeException {

    public DroneNotAvailableException(String serialNumber) {

        super(String.format("Drone with Serial number %s is not available", serialNumber));
    }
}
