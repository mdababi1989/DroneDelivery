package com.mdababi.dronedelivery.config;

import com.mdababi.dronedelivery.exceptions.LowBatteryException;
import com.mdababi.dronedelivery.exceptions.MaxWeightExceededException;
import com.mdababi.dronedelivery.model.*;
import com.mdababi.dronedelivery.services.DeliveryService;
import com.mdababi.dronedelivery.services.DroneService;
import com.mdababi.dronedelivery.services.MedicationService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ApplicationBootstrap implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(ApplicationBootstrap.class);

    private DroneService droneService;
    private MedicationService medicationService;
    private DeliveryService deliveryService;
    @Override
    public void run(String... args) throws Exception {

        // create bootstrap data in the database
        Drone drone1 = new Drone("drone1serialnumber", DroneModel.Lightweight, 200, 56, DroneState.IDLE, null);
        Drone drone2 = new Drone("drone2serialnumber", DroneModel.Cruiserweight, 300, 10, DroneState.IDLE, null);
        Drone drone3 = new Drone("drone3serialnumber", DroneModel.Middleweight, 350, 63, DroneState.IDLE, null);
        Drone drone4 = new Drone("drone4serialnumber", DroneModel.Heavyweight, 500, 73, DroneState.IDLE, null);

        Arrays.asList(drone1, drone2, drone3, drone4).forEach(droneService::saveDrone);

        Medication medication1 = new Medication("CODE1", "medication1", 100);
        Medication medication2 = new Medication("CODE2", "medication2", 106);
        Medication medication3 = new Medication("CODE3", "medication3", 250);
        Medication medication4 = new Medication("CODE4", "medication4", 120);
        Arrays.asList(medication1, medication2, medication3, medication4).forEach(medicationService::addMedication);

        DeliveryDto deliveryDto1 = new DeliveryDto();
        deliveryDto1.setDroneSerialNumber("drone4serialnumber");
        deliveryDto1.setMedicationList(Arrays.asList(medication1, medication2, medication3).stream().map(Medication::getCode).collect(Collectors.toList()));
        deliveryService.createDelivery(deliveryDto1);

        DeliveryDto deliveryDto2 = new DeliveryDto();
        deliveryDto2.setDroneSerialNumber("drone3serialnumber");
        deliveryDto2.setMedicationList(Arrays.asList(medication1, medication2, medication4).stream().map(Medication::getCode).collect(Collectors.toList()));
        deliveryService.createDelivery(deliveryDto2);

        // test Prevent the drone from being in LOADING state if the battery level is below 25% functionality and log error
        DeliveryDto deliveryDto3 = new DeliveryDto();
        deliveryDto3.setDroneSerialNumber("drone2serialnumber");
        deliveryDto3.setMedicationList(Arrays.asList(medication1, medication2, medication4).stream().map(Medication::getCode).collect(Collectors.toList()));
        try {
            deliveryService.createDelivery(deliveryDto3);
        }catch (LowBatteryException e){
            log.error("cant load medication for drone drone2serialnumber! Low battery error");
        }

        // test Prevent the drone from being loaded with more weight that it can carry functionality and log error
        DeliveryDto deliveryDto4 = new DeliveryDto();
        deliveryDto4.setDroneSerialNumber("drone1serialnumber");
        deliveryDto4.setMedicationList(Arrays.asList(medication1, medication2, medication4).stream().map(Medication::getCode).collect(Collectors.toList()));
        try {
            deliveryService.createDelivery(deliveryDto4);
        }catch (MaxWeightExceededException e){
            log.error("cant load medication for drone drone2serialnumber! Max weight exceeded");
        }
    }
}
