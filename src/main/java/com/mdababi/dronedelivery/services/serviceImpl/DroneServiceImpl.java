package com.mdababi.dronedelivery.services.serviceImpl;


import com.mdababi.dronedelivery.model.Drone;
import com.mdababi.dronedelivery.model.DroneState;
import com.mdababi.dronedelivery.model.Medication;
import com.mdababi.dronedelivery.repositories.DroneRepository;
import com.mdababi.dronedelivery.services.DroneService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DroneServiceImpl implements DroneService {
    private DroneRepository droneRepository;

    @Override
    public List<Drone> getDroneList() {
        return droneRepository.findAll();
    }

    @Override
    public Drone findById(String serialNumber) {
        return droneRepository.findById(serialNumber).orElse(null);
    }

    @Override
    public Drone saveDrone(Drone drone) {
        return droneRepository.save(drone);
    }

    @Override
    public Drone updateDrone(Drone drone) {
        return droneRepository.save(drone);
    }

    @Override
    public void deleteDrone(String serialNumber) {
        droneRepository.deleteById(serialNumber);
    }

    @Override
    public List<Medication> loadedMedications(Drone drone) {
        if(drone.getActualDelivery()!=null)
            return drone.getActualDelivery().getMedicationList();
        return null;
    }

    @Override
    public int getBatteryLevel(Drone drone) {
        return drone.getBatteryCapacity();
    }

    @Override
    public List<Drone> getAvailableDrones() {
        return droneRepository.findAll().stream().filter(drone -> drone.getState()== DroneState.IDLE).collect(Collectors.toList());
    }
}
