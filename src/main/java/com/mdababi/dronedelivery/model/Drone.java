package com.mdababi.dronedelivery.model;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Drone implements Serializable {
    @Id
    @Size(max = 100, message = "Serial number size must not exceed 100 characters")
    @Column(name = "serial_number", length=100)
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "model")
    private DroneModel model;

    @Min(value = 1, message = "Weight Limit should not be less than 1")
    @Max(value = 500, message = "Weight Limit should not be greater than 500")
    @Column(name = "weight")
    private Integer weightLimit;

    @Min(value = 0, message = "Battery Capacity should not be less than 0")
    @Max(value = 100, message = "Battery Capacity should not be greater than 100")
    @Column(name = "battery_capacity")
    private Integer batteryCapacity;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private DroneState state;

    @OneToOne
    private Delivery ActualDelivery;

}