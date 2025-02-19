package org.urbanquest.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBadgeResponse {
    private UUID id;
    private UUID badgeId;
    private String badgeName;
    private String badgeDescription;
    private LocalDateTime createdAt;
}
