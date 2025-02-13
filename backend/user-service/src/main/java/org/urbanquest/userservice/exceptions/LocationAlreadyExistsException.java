package org.urbanquest.userservice.exceptions;

import org.springframework.http.HttpStatus;
import org.urbanquest.userservice.models.Location;

public class LocationAlreadyExistsException extends BaseException {

    public LocationAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }

    public static LocationAlreadyExistsException withLocation(Location location) {
        return new LocationAlreadyExistsException(
                String.format("Location already exists with city=%s, state=%s, country=%s",
                        location.getCity(), location.getState(), location.getCountry())
        );
    }
}
