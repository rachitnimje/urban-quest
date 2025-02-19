package org.urbanquest.userservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class BadgeAwardRequest {
    @NotNull(message = "User ID cannot be empty")
    private UUID userId;

    @NotNull(message = "Badge ID cannot be empty")
    private UUID badgeId;
}
