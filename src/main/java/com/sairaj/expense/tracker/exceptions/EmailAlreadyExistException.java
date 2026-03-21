package com.sairaj.expense.tracker.exceptions;

import org.springframework.http.HttpStatus;

public class EmailAlreadyExistException extends RuntimeException {
    public EmailAlreadyExistException(String message) {
        super(message);
    }
    public HttpStatus getStatusCode(){
        return HttpStatus.CONFLICT;
    }
}
