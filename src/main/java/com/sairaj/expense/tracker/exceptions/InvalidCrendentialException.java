package com.sairaj.expense.tracker.exceptions;

import org.springframework.boot.micrometer.observation.autoconfigure.ObservationProperties;
import org.springframework.http.HttpStatus;

public class InvalidCrendentialException extends RuntimeException {
    public InvalidCrendentialException(String message) {
        super(message);
    }
    public HttpStatus getStatusCode(){
        return HttpStatus.UNAUTHORIZED;
    }
}
