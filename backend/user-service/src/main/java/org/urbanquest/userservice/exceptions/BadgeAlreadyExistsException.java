package org.urbanquest.userservice.exceptions;

import org.springframework.http.HttpStatus;
import org.urbanquest.userservice.models.Badge;

public class BadgeAlreadyExistsException extends BaseException {

    public BadgeAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }

    public static BadgeAlreadyExistsException withBadge(Badge badge) {
        return new BadgeAlreadyExistsException(
                String.format("Badge already exists with name=%s, description=%s",
                        badge.getName(), badge.getDescription())
        );
    }
}
