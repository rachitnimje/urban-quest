package org.urbanquest.userservice.exceptions;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends BaseException{
    public UserAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }

    public static UserAlreadyExistsException withEmail(String email) {
        return new UserAlreadyExistsException(
                String.format("User with email '%s' already exists", email)
        );
    }

    public static UserAlreadyExistsException withPhoneNumber(String phoneNumber) {
        return new UserAlreadyExistsException(
                String.format("User with phone number '%s' already exists", phoneNumber)
        );
    }
}
