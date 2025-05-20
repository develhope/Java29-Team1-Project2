package com.develhope.greenripple.exceptions.user;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(Long id) {
        super("User with id " + id + " not found");  // Call the constructor of RuntimeException with the error message
    }
}
