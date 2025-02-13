package org.urbanquest.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.urbanquest.userservice.models.VisitedLocation;

import java.util.List;
import java.util.UUID;

@Repository
public interface VisitedLocationRepository extends JpaRepository<VisitedLocation, UUID> {
    List<VisitedLocation> findByUser_Id(UUID userId);
    List<VisitedLocation> findByLocation_Id(UUID locationId);
}
