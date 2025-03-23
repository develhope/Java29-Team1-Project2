package com.greenripple.green_ripple.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message) {
        super(message);  // Call the constructor of RuntimeException with the error message
    }
}
