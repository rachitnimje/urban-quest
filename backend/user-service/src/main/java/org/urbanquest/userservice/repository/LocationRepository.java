package org.urbanquest.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.urbanquest.userservice.models.Location;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LocationRepository extends JpaRepository<Location, UUID> {
    List<Location> findByActive(boolean active);
    Optional<Location> findByCityAndStateAndCountry(String city, String state, String country);
    List<Location> findByCityContainingIgnoreCaseOrStateContainingIgnoreCaseOrCountryContainingIgnoreCase(
            String cityKeyword, String stateKeyword, String countryKeyword);
}
