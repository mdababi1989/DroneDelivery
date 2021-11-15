package com.mdababi.dronedelivery.controllers;

import com.mdababi.dronedelivery.model.Medication;
import com.mdababi.dronedelivery.services.MedicationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/medications")
@AllArgsConstructor
public class MedicationController {
    private MedicationService medicationService;

    /* List medication items or throw NoDataFoundException*/
    @GetMapping
    public ResponseEntity<List<Medication>> getMedicationList(){
        List<Medication> medicationList = medicationService.getMedicationList();
        return new ResponseEntity<>(medicationList, HttpStatus.OK);
    }

    /* find a medication using its code or throw MedicationNotFoundException*/
    @GetMapping("{code}")
    public ResponseEntity<Medication> findById(@PathVariable("code") String code){
        Medication medication = medicationService.findById(code);
        return new ResponseEntity<Medication>(medication, HttpStatus.OK);
    }

    /* add a medication if data is valid*/
    @PostMapping("add")
    public ResponseEntity<Medication> addMedication(@Valid @RequestBody Medication medication){
        Medication savedMedication = medicationService.addMedication(medication);
        return new ResponseEntity<Medication>(savedMedication, HttpStatus.CREATED);
    }

    /* update a medication item if it exists and data is valid*/
    @PostMapping("updateMedication")
    public ResponseEntity<Medication> updateMedication(@Valid @RequestBody Medication medication){
        Medication medication1 = medicationService.findById(medication.getCode()); // verify that the medication exist in the database
        Medication updatedMedication = medicationService.updateMedication(medication);
        return new ResponseEntity<Medication>(updatedMedication, HttpStatus.OK);
    }

    /* delete a medication item if it exists or throw NoDataFoundException*/
    @DeleteMapping("{code}")
    public ResponseEntity<HttpStatus> deleteMedication(@PathVariable("code") String code){
        Medication medication = medicationService.findById(code);
        medicationService.deleteMedication(code);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
