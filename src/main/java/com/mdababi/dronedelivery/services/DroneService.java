package com.mdababi.dronedelivery.services;



import com.mdababi.dronedelivery.model.Drone;
import com.mdababi.dronedelivery.model.Medication;

import java.util.List;

public interface DroneService {
    List<Drone> getDroneList();
    Drone findById(String serialNumber);
    Drone saveDrone(Drone drone);
    Drone updateDrone(Drone drone);
    void deleteDrone(String serialNumber);
    List<Medication> loadedMedications(Drone drone);
    int getBatteryLevel(Drone drone);
    List<Drone> getAvailableDrones();

}
