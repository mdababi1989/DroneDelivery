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

    @GetMapping
    public ResponseEntity<List<Drone>> getDroneList() {
        List<Drone> droneList = this.droneService.getDroneList();
        return new ResponseEntity<>(droneList, HttpStatus.OK);
    }

    @GetMapping("{serialNumber}")
    public ResponseEntity<Drone> findById(@PathVariable("serialNumber") String serialNumber) {
        Drone drone = droneService.findById(serialNumber);
        return new ResponseEntity<Drone>(drone, HttpStatus.OK);
    }

    @PostMapping("add")
    public ResponseEntity<Drone> saveDrone(@Valid @RequestBody Drone drone) {
        Drone savedDrone = droneService.saveDrone(drone);
        return new ResponseEntity<Drone>(savedDrone, HttpStatus.CREATED);
    }

    @PostMapping("updateDrone")
    public ResponseEntity<Drone> updateDrone(@Valid @RequestBody Drone drone) {
        Drone drone1 = droneService.findById(drone.getSerialNumber()); // verify that the drone exist in the database
        Drone savedDrone = droneService.saveDrone(drone);
        return new ResponseEntity<Drone>(savedDrone, HttpStatus.OK);
    }


    @DeleteMapping("{serialNumber}")
    public ResponseEntity<HttpStatus> deleteDrone(@PathVariable("serialNumber") String serialNumber) {
        Drone drone1 = droneService.findById(serialNumber);
        droneService.deleteDrone(serialNumber);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("getMedications/{serialNumber}")
    public ResponseEntity<List<Medication>> loadedMedications(@PathVariable("serialNumber") String serialNumber) {
        List<Medication> medicationList = droneService.loadedMedications(serialNumber);
        return new ResponseEntity(medicationList, HttpStatus.OK);
    }

    @GetMapping("getBatteryLevel/{serialNumber}")
    public ResponseEntity<Integer> getBatteryLevel(@PathVariable("serialNumber") String serialNumber) {
        Drone drone = droneService.findById(serialNumber);
        return new ResponseEntity<>(drone.getBatteryCapacity(), HttpStatus.OK);
    }

    @GetMapping("availables")
    public ResponseEntity<List<Drone>> getAvailableDrones() {
        List<Drone> droneList = droneService.getAvailableDrones();
        return new ResponseEntity(droneList, HttpStatus.OK);
    }

}
