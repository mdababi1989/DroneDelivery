package com.mdababi.dronedelivery.services;

import com.mdababi.dronedelivery.model.Delivery;

import java.util.List;

public interface DeliveryService {
    List<Delivery> getDeliveryList();
    Delivery saveDelivery(Delivery delivery);
    void deleteDelivery(Long id);
    Delivery createDelivery(String droneSerialNumber, List<String> medicationCodes);
}
