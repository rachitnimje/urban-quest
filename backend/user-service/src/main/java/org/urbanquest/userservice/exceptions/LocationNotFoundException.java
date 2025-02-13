package org.urbanquest.userservice.exceptions;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class LocationNotFoundException extends BaseException {

    public LocationNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

    public static LocationNotFoundException withId(UUID id) {
        return new LocationNotFoundException(
                String.format("Location not found with id %s", id));
    }

    public static LocationNotFoundException withCityStateCountry(String city, String state, String country) {
        return new LocationNotFoundException(
                String.format("Location not found with city=%s, state=%s, country=%s",
                        city, state, country)
        );
    }
}
