package com.mdababi.dronedelivery.services;

import com.mdababi.dronedelivery.model.Delivery;
import com.mdababi.dronedelivery.model.DeliveryDto;

import java.util.List;

public interface DeliveryService {
    List<Delivery> getDeliveryList();
    Delivery createDelivery(DeliveryDto delivery);
}
