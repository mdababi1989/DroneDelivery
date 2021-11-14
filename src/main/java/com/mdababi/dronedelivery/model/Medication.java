package com.mdababi.dronedelivery.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Medication implements Serializable {

    public Medication(String code, String name, double weight) {
        this.code = code;
        this.name = name;
        this.weight = weight;
    }

    @Id
    @Pattern(regexp="^[A-Z0-9_]*$",message="Allowed only upper case letters, underscore and numbers for the medication code")
    @NotEmpty(message = "Please provide a code for the medication")
    @Column(name = "code")
    private String code;

    @Pattern(regexp="^[a-zA-Z0-9_-]*$",message="Allowed only letters, underscore, - and numbers for the name")
    @Column(name = "name")
    @NotEmpty(message = "Please provide a name for the medication")
    private String name;

    @Min(value= 1, message = "Please provide a weight for the medication")
    private double weight;

    private String imageUrl;
}