package org.urbanquest.userservice.services;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.urbanquest.userservice.dto.VisitedLocationResponseDto;
import org.urbanquest.userservice.exceptions.LocationNotFoundException;
import org.urbanquest.userservice.exceptions.UserNotFoundException;
import org.urbanquest.userservice.exceptions.VisitedLocationAlreadyExistsException;
import org.urbanquest.userservice.models.Location;
import org.urbanquest.userservice.models.User;
import org.urbanquest.userservice.models.VisitedLocation;
import org.urbanquest.userservice.repository.LocationRepository;
import org.urbanquest.userservice.repository.UserRepository;
import org.urbanquest.userservice.repository.VisitedLocationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Validated
@Slf4j
public class VisitedLocationService {
    private final VisitedLocationRepository visitedLocationRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;

    public VisitedLocationService(VisitedLocationRepository visitedLocationRepository, UserRepository userRepository, LocationRepository locationRepository) {
        this.visitedLocationRepository = visitedLocationRepository;
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
    }

    public VisitedLocation addVisitedLocation(@NotNull UUID userId, @NotNull UUID locationId) {
        log.info("Adding Visited Location {} for User {}", locationId, userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserNotFoundException.withId(userId));

        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> LocationNotFoundException.withId(userId));

        if(visitedLocationRepository.findByUser_IdAndLocation_Id(
                user.getId(),
                location.getId())
                .isPresent()) {
            log.error("Visited Location {} already exists", locationId);
            throw VisitedLocationAlreadyExistsException.withUserAndLocation(
                    userId,
                    locationId);
        }

        VisitedLocation newVisitedLocation = new VisitedLocation();
        newVisitedLocation.setUser(user);
        newVisitedLocation.setLocation(location);

        VisitedLocation savedVisitedLocation = visitedLocationRepository.save(newVisitedLocation);
        log.info("Visited Location {} added successfully", savedVisitedLocation);
        return savedVisitedLocation;
    }

    public List<VisitedLocationResponseDto> getVisitedLocations(@NotNull UUID userId) {
        log.info("Getting Visited Locations for User {}", userId);

        List<VisitedLocation> visitedLocations = visitedLocationRepository.findByUser_Id(userId);
        List<VisitedLocationResponseDto> responseDtos = new ArrayList<>();

        for(VisitedLocation visitedLocation : visitedLocations) {
            VisitedLocationResponseDto responseDto = VisitedLocationResponseDto.from(visitedLocation);
            responseDtos.add(responseDto);
        }

        log.info("Visited Locations {}", responseDtos);
        return responseDtos;
    }
}
