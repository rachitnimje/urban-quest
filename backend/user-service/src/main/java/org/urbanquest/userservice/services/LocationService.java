package org.urbanquest.userservice.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.urbanquest.userservice.exceptions.LocationAlreadyExistsException;
import org.urbanquest.userservice.exceptions.LocationNotFoundException;
import org.urbanquest.userservice.models.Location;
import org.urbanquest.userservice.repository.LocationRepository;

import java.util.List;
import java.util.UUID;

@Service
@Validated
public class LocationService {
    private static final Logger logger = LoggerFactory.getLogger(LocationService.class);
    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Location addLocation(Location location) {
        logger.debug("Adding new location: {}", location);

        // check if the location already exists
        if(locationRepository.findByCityAndStateAndCountry(
                location.getCity(),
                location.getState(),
                location.getCountry())
                .isPresent()) {
            logger.error("Location already exists {}", location);
            throw LocationAlreadyExistsException.withLocation(location);
        }

        // create if it does not exist
        Location savedLocation = locationRepository.save(location);
        logger.info("Location saved successfully {}", savedLocation);
        return savedLocation;
    }

    public Location getLocationByCityStateCountry(
            String city,
            String state,
            String country) {
        logger.debug("Fetching location by city : {} state: {} and country: {}", city, state, country);

        return locationRepository.findByCityAndStateAndCountry(city, state, country)
                .orElseThrow(() -> LocationNotFoundException.withCityStateCountry(city, state, country));
    }

    public Location getLocationById(UUID id) {
        logger.debug("Fetching location by id : {}", id);

        return locationRepository.findById(id)
                .orElseThrow(() -> LocationNotFoundException.withId(id));
    }

    public List<Location> getLocations(boolean active) {
        logger.debug("Fetching all locations with status: {}", active);
        return locationRepository.findByActive(active);
    }

    public Location updateLocation(UUID id, Location location) {
        logger.debug("Updating location: {}", location);

        Location existingLocation = getLocationById(id);

        existingLocation.setCity(location.getCity());
        existingLocation.setState(location.getState());
        existingLocation.setCountry(location.getCountry());
        existingLocation.setLatitude(location.getLatitude());
        existingLocation.setLongitude(location.getLongitude());
        existingLocation.setGoogleMapsUrl(location.getGoogleMapsUrl());
        existingLocation.setActive(location.isActive());

        Location updatedLocation = locationRepository.save(existingLocation);
        logger.info("Location updated successfully {}", updatedLocation);
        return updatedLocation;
    }

    public void deleteLocation(UUID id) {
        logger.debug("Deleting location by id : {}", id);

        Location location = locationRepository.findById(id)
                .orElseThrow(() -> LocationNotFoundException.withId(id));

        locationRepository.delete(location);
        logger.info("Location deleted successfully {}", location);
    }

    public List<Location> getAllLocations() {
        logger.debug("Fetching all locations");
        return locationRepository.findAll();
    }

    public List<Location> getLocationByKeyword(String keyword) {
        return locationRepository.findByCityContainingIgnoreCaseOrStateContainingIgnoreCaseOrCountryContainingIgnoreCase(keyword, keyword, keyword);
    }
}
