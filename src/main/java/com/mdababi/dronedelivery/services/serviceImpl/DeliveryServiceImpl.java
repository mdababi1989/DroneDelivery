package com.mdababi.dronedelivery.services.serviceImpl;


import com.mdababi.dronedelivery.model.Delivery;
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
        return deliveryRepository.findAll();
    }

    @Override
    public Delivery saveDelivery(Delivery delivery) {
        return deliveryRepository.save(delivery);
    }

    @Override
    public void deleteDelivery(Long id) {
        deliveryRepository.deleteById(id);
    }

    @Override
    public Delivery createDelivery(String droneSerialNumber, List<String> medicationCodes) {

        Drone drone = droneService.findById(droneSerialNumber);
        if(drone == null) throw new EntityNotFoundException("there is no drone with serial number: "+droneSerialNumber);
        List<Medication> medicationList = new ArrayList<>();
        for(String medicationCode: medicationCodes){
            Medication medication = medicationService.findById(medicationCode);
            if(medication == null )throw new EntityNotFoundException("there is no medication with code: "+medicationCode);
            medicationList.add(medication);
        }

        Delivery delivery = new Delivery(drone, medicationList);
        Delivery savedDelivery = saveDelivery(delivery);
        drone.setActualDelivery(delivery);
        droneService.updateDrone(drone);
        return savedDelivery;
    }
}
