package org.urbanquest.userservice.exceptions;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class UserNotFoundException extends BaseException {

    public UserNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

    public static UserNotFoundException withId(UUID id) {
        return new UserNotFoundException(
                String.format("User not found with id %s", id)
        );
    }

    public static UserNotFoundException withEmail(String email) {
        return new UserNotFoundException(
                String.format("User not found with email %s", email)
        );
    }

    public static UserNotFoundException withPhoneNumber(String phoneNumber) {
        return new UserNotFoundException(
                String.format("User not found with phone number %s", phoneNumber)
        );
    }
}
