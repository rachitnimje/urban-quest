package org.urbanquest.userservice.exceptions;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class BadgeNotFoundException extends BaseException {
    public BadgeNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

    public static BadgeNotFoundException withId(UUID id) {
        return new BadgeNotFoundException(
                String.format("Badge not found with id %s ", id)
        );
    }
}
