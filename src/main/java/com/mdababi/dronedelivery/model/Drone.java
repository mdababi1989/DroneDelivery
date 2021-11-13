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
public class Drone implements Serializable {
    @Id
    @Size(max = 100, message = "Serial number size must not exceed 100 characters")
    @Column(name = "serial_number", length=50)
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "model")
    private DroneModel model;

    @Max(500)
    @Min(0)
    @NotEmpty(message = "Please provide a weight limit for the drone")
    @Column(name = "weight")
    private int weightLimit;

    @Max(100)
    @Min(0)
    @Column(name = "battery_capacity")
    private int batteryCapacity;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private DroneState state;

    @OneToOne
    private Delivery ActualDelivery;

}