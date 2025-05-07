package com.develhope.greenripple.exceptions;

import com.develhope.greenripple.exceptions.reward.RewardNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler  {

    @ExceptionHandler(RewardNotFoundException.class)
    public ResponseEntity<String> handleRewardNotFoundException(RewardNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal Server Error: " + ex.getMessage());
    }
}
