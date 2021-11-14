package com.mdababi.dronedelivery.exceptions;

public class MedicationNotFoundException extends RuntimeException {

    public MedicationNotFoundException(String serialNumber) {

        super(String.format("medication with code %s not found", serialNumber));
    }
}