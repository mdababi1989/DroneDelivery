package com.mdababi.dronedelivery.services.serviceImpl;


import com.mdababi.dronedelivery.exceptions.*;
import com.mdababi.dronedelivery.model.Delivery;
import com.mdababi.dronedelivery.model.DeliveryDto;
import com.mdababi.dronedelivery.model.Drone;
import com.mdababi.dronedelivery.model.Medication;
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

        Drone drone = droneService.findById(deliveryDto.getDroneSerialNumber());
        if (drone == null) throw new DroneNotFoundException(deliveryDto.getDroneSerialNumber());
        if (drone.getBatteryCapacity() < 25)
            throw new LowBatteryException("Medication can't be loaded! Drone battery capacity is under 25%");
        List<Medication> medicationList = new ArrayList<>();
        int medicationWeightSum = 0;
        for (String medicationCode : deliveryDto.getMedicationList()) {
            Medication medication = medicationService.findById(medicationCode);
            if (medication == null) throw new MedicationNotFoundException(medicationCode);
            medicationList.add(medication);
            medicationWeightSum += medication.getWeight();
        }
        if (medicationWeightSum > drone.getWeightLimit())
            throw new MaxWeightExceededException("Can't load medications. Max weight ( " + drone.getWeightLimit() + " )exceeded!");
        Delivery delivery = new Delivery(drone, medicationList);
        Delivery savedDelivery = deliveryRepository.save(delivery);
        drone.setActualDelivery(delivery);
        droneService.updateDrone(drone);
        return savedDelivery;
    }
}
