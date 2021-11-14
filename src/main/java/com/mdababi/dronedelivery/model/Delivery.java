package com.mdababi.dronedelivery.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Delivery implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long deliveryId;
    @ManyToOne
    private Drone drone;
    @OneToMany
    private List<Medication> medicationList = new ArrayList<>();

    public Delivery(Drone drone, List<Medication> medicationList) {
    }
}
