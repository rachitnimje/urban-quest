package org.urbanquest.userservice.exceptions;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class BadgeAlreadyAwardedException extends BaseException{
    public BadgeAlreadyAwardedException(String message) {
        super(message, HttpStatus.CONFLICT);
    }

    public static BadgeAlreadyAwardedException withId(UUID userId, UUID badgeId) {
        return new BadgeAlreadyAwardedException(
                String.format("User %s has already been awarded Badge %s", userId, badgeId)
        );
    }
}
