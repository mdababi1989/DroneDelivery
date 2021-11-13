package com.mdababi.dronedelivery.services.serviceImpl;


import com.mdababi.dronedelivery.model.Medication;
import com.mdababi.dronedelivery.repositories.MedicationRepository;
import com.mdababi.dronedelivery.services.MedicationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MedicationServiceImpl implements MedicationService {
    private MedicationRepository medicationRepository;

    @Override
    public List<Medication> getMedicationList() {
        return medicationRepository.findAll();
    }

    @Override
    public Medication findById(String code) {
        return medicationRepository.findById(code).orElse(null);
    }

    @Override
    public Medication addMedication(Medication medication) {
        return medicationRepository.save(medication);
    }

    @Override
    public Medication updateMedication(Medication medication) {
        return medicationRepository.save(medication);
    }

    @Override
    public void deleteMedication(String code) {
        medicationRepository.deleteById(code);
    }
}
