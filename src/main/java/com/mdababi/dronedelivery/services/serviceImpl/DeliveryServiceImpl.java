package com.mdababi.dronedelivery.services.serviceImpl;


import com.mdababi.dronedelivery.exceptions.*;
import com.mdababi.dronedelivery.model.*;
import com.mdababi.dronedelivery.repositories.DeliveryRepository;
import com.mdababi.dronedelivery.services.DeliveryService;
import com.mdababi.dronedelivery.services.DroneService;
import com.mdababi.dronedelivery.services.MedicationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {
    private DeliveryRepository deliveryRepository;
    private DroneService droneService;
    private MedicationService medicationService;

    @Override
    public List<Delivery> getDeliveryList() {
        List<Delivery> deliveryList = deliveryRepository.findAll();
        if (deliveryList.isEmpty()) throw new NoDataFoundException("Delivery");
        return deliveryList;
    }

    @Override
    public Delivery createDelivery(DeliveryDto deliveryDto) {
        /* Verify that the drone exist */
        Drone drone = droneService.findById(deliveryDto.getDroneSerialNumber());
        if (drone == null) throw new DroneNotFoundException(deliveryDto.getDroneSerialNumber());
        /* Verify that the drone is available */
        if(drone.getState() != DroneState.IDLE) throw new DroneNotAvailableException(drone.getSerialNumber());
        /* Verify that that battery level is above 24% */
        if (drone.getBatteryCapacity() < 25)
            throw new LowBatteryException("Medication can't be loaded! Drone battery capacity is under 25%");
        /* Verify that the medications items exist */
        List<Medication> medicationList = new ArrayList<>();
        int medicationWeightSum = 0;
        for (String medicationCode : deliveryDto.getMedicationList()) {
            Medication medication = medicationService.findById(medicationCode);
            if (medication == null) throw new MedicationNotFoundException(medicationCode);
            medicationList.add(medication);
            medicationWeightSum += medication.getWeight();
        }
        /* verify that the medication items total weight is less than the drone maximum weight**/
        if (medicationWeightSum > drone.getWeightLimit())
            throw new MaxWeightExceededException("Can't load medications. Max weight ( " + drone.getWeightLimit() + " )exceeded!");
        /* The drone pass to the loading state (waiting for medications items to be loaded) */
        drone.setState(DroneState.LOADING);
        droneService.saveDrone(drone);
        /* Create the delivery */
        Delivery delivery = new Delivery(drone, medicationList);
        Delivery savedDelivery = deliveryRepository.save(delivery);
        /* Add the delivery to the drone. The drone pass to the LOADED state  */
        drone.setActualDelivery(delivery);
        drone.setState(DroneState.LOADED);
        droneService.updateDrone(drone);
        return savedDelivery;
    }
}
