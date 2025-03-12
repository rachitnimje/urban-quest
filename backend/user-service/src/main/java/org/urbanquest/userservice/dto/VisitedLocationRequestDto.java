package org.urbanquest.userservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisitedLocationRequestDto {
    @NotNull(message = "User ID cannot be empty")
    private UUID userId;

    @NotNull(message = "Location ID cannot be empty")
    private UUID locationId;
}
