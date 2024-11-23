package com.codewiz.stockadvisor.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleException(Exception e) {
        return ResponseEntity.internalServerError().body(new ErrorMessage(101, "Internal Error. Please contact support"));
    }

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<ErrorMessage> handleNoDataFoundException(NoDataFoundException e) {
        return ResponseEntity.badRequest().body(new ErrorMessage(102,"No data found"));
    }
}
