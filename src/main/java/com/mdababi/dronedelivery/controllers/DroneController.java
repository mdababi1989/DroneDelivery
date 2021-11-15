package com.mdababi.dronedelivery.controllers;

import com.mdababi.dronedelivery.model.Drone;
import com.mdababi.dronedelivery.model.Medication;
import com.mdababi.dronedelivery.services.DroneService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/drones")
@AllArgsConstructor
public class DroneController {
    private DroneService droneService;

    /*
    Get drone list from database
    */
    @GetMapping
    public ResponseEntity<List<Drone>> getDroneList() {
        List<Drone> droneList = this.droneService.getDroneList();
        return new ResponseEntity<>(droneList, HttpStatus.OK);
    }

    /*
    find drone in the database using its serial number
       return the drone or throw DroneNotFoundException
    */
    @GetMapping("{serialNumber}")
    public ResponseEntity<Drone> findById(@PathVariable("serialNumber") String serialNumber) {
        Drone drone = droneService.findById(serialNumber);
        return new ResponseEntity<>(drone, HttpStatus.OK);
    }

    /*
        create drone in the database if data is valid
    */
    @PostMapping("add")
    public ResponseEntity<Drone> saveDrone(@Valid @RequestBody Drone drone) {
        Drone savedDrone = droneService.saveDrone(drone);
        return new ResponseEntity<>(savedDrone, HttpStatus.CREATED);
    }

    /*
    update drone in the database if it exist and data is valid
    */
    @PostMapping("updateDrone")
    public ResponseEntity<Drone> updateDrone(@Valid @RequestBody Drone drone) {
        Drone drone1 = droneService.findById(drone.getSerialNumber()); // verify that the drone exist in the database
        Drone savedDrone = droneService.saveDrone(drone);
        return new ResponseEntity<Drone>(savedDrone, HttpStatus.OK);
    }

    /*
    delete drone from the database using its serial number if it exist or throw DroneNotFoundException
    */
    @DeleteMapping("{serialNumber}")
    public ResponseEntity<HttpStatus> deleteDrone(@PathVariable("serialNumber") String serialNumber) {
        Drone drone1 = droneService.findById(serialNumber);
        droneService.deleteDrone(serialNumber);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /*
    list medication items for a given drone if it exist or throw DroneNotFoundException
    If list is empty ( the drone is available ) throw NoDataFoundException
    */
    @GetMapping("getMedications/{serialNumber}")
    public ResponseEntity<List<Medication>> loadedMedications(@PathVariable("serialNumber") String serialNumber) {
        List<Medication> medicationList = droneService.loadedMedications(serialNumber);
        return new ResponseEntity(medicationList, HttpStatus.OK);
    }

    /*
    get battery capacity for a given drone or throw DroneNotFoundException
    */
    @GetMapping("getBatteryLevel/{serialNumber}")
    public ResponseEntity<Integer> getBatteryLevel(@PathVariable("serialNumber") String serialNumber) {
        Drone drone = droneService.findById(serialNumber);
        return new ResponseEntity<>(drone.getBatteryCapacity(), HttpStatus.OK);
    }

    /*
    list available drones. If no drone is available to carry medication throw NoDataFoundException
    */
    @GetMapping("availables")
    public ResponseEntity<List<Drone>> getAvailableDrones() {
        List<Drone> droneList = droneService.getAvailableDrones();
        return new ResponseEntity(droneList, HttpStatus.OK);
    }

}
