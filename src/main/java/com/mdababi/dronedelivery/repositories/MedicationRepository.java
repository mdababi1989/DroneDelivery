package com.mdababi.dronedelivery.repositories;

import com.mdababi.dronedelivery.model.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, String> {
}
