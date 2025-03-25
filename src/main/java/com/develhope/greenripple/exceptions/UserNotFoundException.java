package com.develhope.greenripple.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message) {
        super(message);  // Call the constructor of RuntimeException with the error message
    }
}
