package org.urbanquest.userservice.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.urbanquest.userservice.dto.ApiResponse;
import org.urbanquest.userservice.models.Location;
import org.urbanquest.userservice.services.LocationService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/locations")
@Validated
public class LocationController {

    final private LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<Location>>> getAllLocations() {
        List<Location> allLocations = locationService.getAllLocations();

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "All locations retrieved successfully",
                allLocations
        ));
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse<Location>> addLocation(@Valid @RequestBody Location location) {
        Location savedLocation = locationService.addLocation(location);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Location created successfully",
                savedLocation
        ));
    }

    @GetMapping("/{city}/{state}/{country}")
    public ResponseEntity<ApiResponse<Location>> getLocation(
            @NotNull @PathVariable String city,
            @NotNull @PathVariable String state,
            @NotNull @PathVariable String country) {

        Location location = locationService.getLocationByCityStateCountry(city, state, country);
        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Location retrieved successfully",
                location
        ));
    }

    @GetMapping("search")
    public ResponseEntity<ApiResponse<List<Location>>> getLocationByKeyword(@NotNull @RequestParam String keyword) {
        List<Location> location = locationService.getLocationByKeyword(keyword.trim());

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Location retrieved successfully",
                location
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Location>> getLocation(@PathVariable @NotNull UUID id) {
        Location location = locationService.getLocationById(id);
        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Location retrieved successfully",
                location
        ));
    }

    @GetMapping("/active/{status}")
    public ResponseEntity<ApiResponse<List<Location>>> getLocations(@PathVariable boolean status) {
        List<Location> activeLocations = locationService.getLocations(status);
        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Locations retrieved successfully",
                activeLocations
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Location>> updateLocation(
            @PathVariable @NotNull UUID id,
            @Valid @RequestBody Location location) {
        Location updatedLocation = locationService.updateLocation(id, location);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse<>(
                HttpStatus.NO_CONTENT.value(),
                "Location updated successfully",
                updatedLocation
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteLocation(@PathVariable @NotNull UUID id) {
        locationService.deleteLocation(id);
        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.NO_CONTENT.value(),
                "Location deleted successfully",
                null
        ));
    }
}
