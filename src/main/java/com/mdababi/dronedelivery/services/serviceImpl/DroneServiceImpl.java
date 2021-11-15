package com.mdababi.dronedelivery.services.serviceImpl;


import com.mdababi.dronedelivery.exceptions.DroneNotFoundException;
import com.mdababi.dronedelivery.exceptions.NoDataFoundException;
import com.mdababi.dronedelivery.model.Drone;
import com.mdababi.dronedelivery.model.DroneState;
import com.mdababi.dronedelivery.model.Medication;
import com.mdababi.dronedelivery.repositories.DroneRepository;
import com.mdababi.dronedelivery.services.DroneService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DroneServiceImpl implements DroneService {
    private static final Logger log = LoggerFactory.getLogger(DroneServiceImpl.class);

    private DroneRepository droneRepository;

    @Override
    public List<Drone> getDroneList() {
        List<Drone> droneList = droneRepository.findAll();
        if (droneList.isEmpty()) throw new NoDataFoundException("Drone");
        return droneList;
    }

    @Scheduled(fixedRate = 300000) // every 5 minutes
    public void reportDronesBatteryLevel() {
        droneRepository.findAll().forEach(
                drone -> log.info("Drone Serial Number {}, Battery level: {}", drone.getSerialNumber(), drone.getBatteryCapacity())
        );
    }

    @Override
    public Drone findById(String serialNumber) {
        return droneRepository.findById(serialNumber).orElseThrow(() -> new DroneNotFoundException(serialNumber));
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
    public List<Medication> loadedMedications(String serialNumber) {
        Drone drone = findById(serialNumber);
        if (drone.getActualDelivery() == null) {
            throw new NoDataFoundException("Medication");
        }
        return drone.getActualDelivery().getMedicationList();
    }

    @Override
    public int getBatteryLevel(String serialNumber) {
        Drone drone = findById(serialNumber);
        if (drone == null) throw new EntityNotFoundException("there is no drone with serial number: " + serialNumber);
        return drone.getBatteryCapacity();
    }

    @Override
    public List<Drone> getAvailableDrones() {
        List<Drone> droneList = droneRepository.findAll();
        if (droneList.isEmpty()) throw new NoDataFoundException("Drone");
        return droneList.stream().filter(drone -> drone.getState() == DroneState.IDLE).collect(Collectors.toList());
    }
}
