package com.sairaj.expense.tracker.exceptions;

import com.sairaj.expense.tracker.dto.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<?> handleEmailAlreadyExistException(EmailAlreadyExistException emailAlreadyExistException){
        ErrorResponse errorResponse = ErrorResponse.builder().message(emailAlreadyExistException.getMessage()).date(LocalDate.now()).build();
        return ResponseEntity
                .status(emailAlreadyExistException.getStatusCode())
                .body(errorResponse);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException){
        ErrorResponse errorResponse = ErrorResponse.builder().message(methodArgumentNotValidException.getMessage()).date(LocalDate.now()).build();
        return ResponseEntity
                .status(methodArgumentNotValidException.getStatusCode())
                .body(errorResponse);
    }
    @ExceptionHandler(InvalidCrendentialException.class)
    public ResponseEntity<?> handleInvalidCrendentialException(InvalidCrendentialException invalidCrendentialException){
        ErrorResponse errorResponse = ErrorResponse.builder().message(invalidCrendentialException.getMessage()).date(LocalDate.now()).build();
        return ResponseEntity
                .status(invalidCrendentialException.getStatusCode())
                .body(errorResponse);
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleGenericException(EntityNotFoundException exception){
        ErrorResponse errorResponse = ErrorResponse.builder().message(exception.getMessage()).date(LocalDate.now()).build();
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception exception){
        ErrorResponse errorResponse = ErrorResponse.builder().message(exception.getMessage()).date(LocalDate.now()).build();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }
}
