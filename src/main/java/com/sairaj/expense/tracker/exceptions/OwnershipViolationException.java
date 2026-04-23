package com.sairaj.expense.tracker.exceptions;

import org.springframework.http.HttpStatus;

public class OwnershipViolationException extends RuntimeException {
  public OwnershipViolationException(String message) {
    super(message);
  }
  public HttpStatus getStatusCode(){ return HttpStatus.UNAUTHORIZED; }
}
