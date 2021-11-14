package com.mdababi.dronedelivery.controllers;

import com.mdababi.dronedelivery.model.Delivery;
import com.mdababi.dronedelivery.model.DeliveryDto;
import com.mdababi.dronedelivery.services.DeliveryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/deliveries")
@AllArgsConstructor
public class DeliveryController {
    private DeliveryService deliveryService;

    @GetMapping
    public ResponseEntity<List<Delivery>> getDeliveryList(){
        List<Delivery> deliveryList = deliveryService.getDeliveryList();
        return new ResponseEntity<>(deliveryList, HttpStatus.OK);
    }

    @PostMapping("add")
    public ResponseEntity<Delivery> addDelivery(@RequestBody DeliveryDto delivery){
        Delivery savedDelivery = deliveryService.createDelivery(delivery);
        return new ResponseEntity<Delivery>(savedDelivery, HttpStatus.CREATED);
    }
}
