package com.mdababi.dronedelivery.exceptions;

public class MaxWeightExceededException extends RuntimeException{
    public MaxWeightExceededException(String message) {
        super(message);
    }
}
