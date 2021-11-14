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

    @GetMapping
    public ResponseEntity<List<Medication>> getMedicationList(){
        List<Medication> medicationList = medicationService.getMedicationList();
        return new ResponseEntity<>(medicationList, HttpStatus.OK);
    }

    @GetMapping("{code}")
    public ResponseEntity<Medication> findById(@PathVariable("code") String code){
        Medication medication = medicationService.findById(code);
        return new ResponseEntity<Medication>(medication, HttpStatus.OK);
    }

    @PostMapping("add")
    public ResponseEntity<Medication> addMedication(@Valid @RequestBody Medication medication){
        Medication savedMedication = medicationService.addMedication(medication);
        return new ResponseEntity<Medication>(savedMedication, HttpStatus.CREATED);
    }

    @PostMapping("updateMedication")
    public ResponseEntity<Medication> updateMedication(@Valid @RequestBody Medication medication){
        Medication medication1 = medicationService.findById(medication.getCode()); // verify that the medication exist in the database
        Medication updatedMedication = medicationService.updateMedication(medication);
        return new ResponseEntity<Medication>(updatedMedication, HttpStatus.OK);
    }

    @DeleteMapping("{code}")
    public ResponseEntity<HttpStatus> deleteMedication(@PathVariable("code") String code){
        Medication medication = medicationService.findById(code);
        medicationService.deleteMedication(code);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
