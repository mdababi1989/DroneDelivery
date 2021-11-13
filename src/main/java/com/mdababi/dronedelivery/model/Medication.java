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
    @Id
    @Pattern(regexp="^[A-Z0-9_]",message="Allowed only upper case letters, underscore and numbers for the medication code")
    @NotEmpty(message = "Please provide a code for the medication")
    @Column(name = "code")
    private String code;

    @Pattern(regexp="^[a-zA-Z0-9_-]",message="Allowed only letters, underscore, - and numbers for the name")
    @Column(name = "name")
    @NotEmpty(message = "Please provide a name for the medication")
    private String name;

    @NotEmpty(message = "Please provide a weight for the medication")
    @Min(0)
    private double weight;

    private String imageUrl;
}