package com.mdababi.dronedelivery.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/*
This class is used as a Global Exception Handling and apply them across the whole application
 */
@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(DroneNotFoundException.class)
    public ResponseEntity<Object> handleDroneNotFoundException(
            DroneNotFoundException ex, WebRequest request) {
        return getObjectResponseEntity(ex.getMessage());
    }

    @ExceptionHandler(DroneNotAvailableException.class)
    public ResponseEntity<Object> handleDroneNotAvailableException(
            DroneNotAvailableException ex, WebRequest request) {
        return getObjectResponseEntity(ex.getMessage());
    }

    @ExceptionHandler(MedicationNotFoundException.class)
    public ResponseEntity<Object> handleMedicationNotFoundException(
            MedicationNotFoundException ex, WebRequest request) {
        return getObjectResponseEntity(ex.getMessage());
    }

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<Object> handleNodataFoundException(
            NoDataFoundException ex, WebRequest request) {
        return getObjectResponseEntity(ex.getMessage());
    }

    @ExceptionHandler(LowBatteryException.class)
    public ResponseEntity<Object> handleLowBatteryException(
            LowBatteryException ex, WebRequest request) {
        return getObjectResponseEntityBadRequest(ex.getMessage());
    }

    @ExceptionHandler(MaxWeightExceededException.class)
    public ResponseEntity<Object> handleMaxWeightExceededException(
            MaxWeightExceededException ex, WebRequest request) {
        return getObjectResponseEntityBadRequest(ex.getMessage());
    }

    private ResponseEntity<Object> getObjectResponseEntityBadRequest(String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", "400");
        body.put("error", "Bad Request");
        body.put("message", message);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> getObjectResponseEntity(String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", "404");
        body.put("error", "Not Found");
        body.put("message", message);

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleValidationExceptions(
            Exception e, WebRequest request) {
        Map<String, String> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", "400");
        body.put("error", "Bad Request");

        BindingResult bindingResult = null;
        if (e instanceof BindException) {
            // Validated by @Valid
            bindingResult = ((BindException) e).getBindingResult();
        } else if (e instanceof MethodArgumentNotValidException) {
            // Validated by @Validated for @RequestParam validatation
            bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
        }

        bindingResult.getFieldErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage() + " ( rejected value: " + ((FieldError) error).getRejectedValue() + " )";
            body.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }


}
