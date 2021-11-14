package com.mdababi.dronedelivery.exceptions;

public class DroneNotFoundException extends RuntimeException {

    public DroneNotFoundException(String serialNumber) {

        super(String.format("Drone with Serial number %s not found", serialNumber));
    }
}