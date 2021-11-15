package com.mdababi.dronedelivery.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/*
class used for creating a delivery
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryDto {
    private String DroneSerialNumber; // drone serial number
    private List<String> medicationList; // code list for medications items
}
