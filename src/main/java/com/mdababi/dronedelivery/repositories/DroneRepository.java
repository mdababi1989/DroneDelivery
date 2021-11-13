package com.mdababi.dronedelivery.repositories;

import com.mdababi.dronedelivery.model.Drone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DroneRepository extends JpaRepository<Drone, String> {
}
