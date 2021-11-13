package com.mdababi.dronedelivery.services;

import com.mdababi.dronedelivery.model.Medication;

import java.util.List;

public interface MedicationService {
    List<Medication> getMedicationList();
    Medication findById(String code);
    Medication addMedication(Medication medication);
    Medication updateMedication(Medication medication);
    void deleteMedication(String code);

}
