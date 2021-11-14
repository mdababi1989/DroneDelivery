package com.mdababi.dronedelivery.exceptions;

public class NoDataFoundException extends RuntimeException {

    public NoDataFoundException(String message) {
        super("No data found. "+message+" List is empty!");
    }
}