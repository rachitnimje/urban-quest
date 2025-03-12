package org.urbanquest.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.urbanquest.userservice.models.VisitedLocation;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitedLocationResponseDto {
    private UUID id;
    private UUID locationId;
    private String city;
    private String state;
    private String country;
    private String googleMapsUrl;
    private LocalDateTime visitedAt;

    public static VisitedLocationResponseDto from(VisitedLocation visitedLocation) {
        VisitedLocationResponseDto responseDto = new VisitedLocationResponseDto();

        responseDto.setId(visitedLocation.getId());
        responseDto.setCity(visitedLocation.getLocation().getCity());
        responseDto.setState(visitedLocation.getLocation().getState());
        responseDto.setCountry(visitedLocation.getLocation().getCountry());
        responseDto.setGoogleMapsUrl(visitedLocation.getLocation().getGoogleMapsUrl());
        responseDto.setVisitedAt(visitedLocation.getVisitedAt());

        return responseDto;
    }
}
