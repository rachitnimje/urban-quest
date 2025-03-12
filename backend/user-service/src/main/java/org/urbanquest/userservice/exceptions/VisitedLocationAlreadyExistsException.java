package org.urbanquest.userservice.exceptions;

import org.springframework.http.HttpStatus;
import org.urbanquest.userservice.models.Location;
import org.urbanquest.userservice.models.User;

import java.util.UUID;

public class VisitedLocationAlreadyExistsException extends BaseException {
    public VisitedLocationAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }

    public static VisitedLocationAlreadyExistsException withUserAndLocation(UUID userId, UUID locationId) {
        return new VisitedLocationAlreadyExistsException(
                String.format("User %s already visited at location %s", userId, locationId)
        );
    }
}
