package com.mdababi.dronedelivery.services.serviceImpl;


import com.mdababi.dronedelivery.exceptions.DroneNotFoundException;
import com.mdababi.dronedelivery.exceptions.MedicationNotFoundException;
import com.mdababi.dronedelivery.exceptions.NoDataFoundException;
import com.mdababi.dronedelivery.model.Drone;
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
        List<Medication> medicationList = medicationRepository.findAll();
        if (medicationList.isEmpty()) throw new NoDataFoundException("Medication");
        return medicationList;
    }

    @Override
    public Medication findById(String code) {
        return medicationRepository.findById(code).orElseThrow(() -> new MedicationNotFoundException(code));
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
        Medication medication = findById(code);
        medicationRepository.deleteById(code);
    }
}
