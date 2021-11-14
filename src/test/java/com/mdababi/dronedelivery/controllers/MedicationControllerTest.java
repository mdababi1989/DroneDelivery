package com.mdababi.dronedelivery.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdababi.dronedelivery.exceptions.MedicationNotFoundException;
import com.mdababi.dronedelivery.exceptions.NoDataFoundException;
import com.mdababi.dronedelivery.model.Medication;
import com.mdababi.dronedelivery.services.MedicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class MedicationControllerTest {


    @MockBean
    private MedicationService medicationService;
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Medication medication1, medication2, invalidMedication;
    List<Medication> medicationList;

    @BeforeEach
    void setUp() {
        medication1 = new Medication("MEDICATION_1CODE", "medication1_", 240);
        medication2 = new Medication("MEDICATION2_CODE", "medication2-", 105);
        //code contain invalid '-' and name contain invalid '$'
        invalidMedication = new Medication("medication2Code-", "medication2$", 105);
        medicationList = new ArrayList<>(Arrays.asList(medication1, medication2));
    }


    @Test
    void getMedicationListNotEmpty() throws Exception {
        when(medicationService.getMedicationList()).thenReturn(medicationList);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/api/v1/medications"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$[*]", hasSize(2)))
                .andExpect(jsonPath("$[1].code").value("MEDICATION2_CODE"));
    }

    @Test
    void getMedicationListEmpty() throws Exception {
        when(medicationService.getMedicationList()).thenThrow(new NoDataFoundException("Medication"));
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/api/v1/medications"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void findByIdOk() throws Exception {
        when(medicationService.findById("MEDICATION_1CODE")).thenReturn(medication1);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/api/v1/medications/MEDICATION_1CODE"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.code").value("MEDICATION_1CODE"))
                .andExpect(jsonPath("$.name").value("medication1_"))
                .andExpect(jsonPath("$.weight").value(240));
    }

    @Test
    void findByIdNotFound() throws Exception {
        when(medicationService.findById("MEDICATION_1CODE")).thenThrow(new MedicationNotFoundException("MEDICATION_1CODE"));
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/api/v1/medications/MEDICATION_1CODE"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void saveValidMedication_OkResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/medications/add")
                .content(objectMapper.writeValueAsString(medication1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void saveInvalidMedication_BadResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/medications/add")
                .content(objectMapper.writeValueAsString(invalidMedication))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect( jsonPath("$.code").value("Allowed only upper case letters, underscore and numbers for the medication code ( rejected value: medication2Code- )"))
                .andExpect( jsonPath("$.name").value("Allowed only letters, underscore, - and numbers for the name ( rejected value: medication2$ )"));
    }

    @Test
    void deleteMedicationOk() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.delete("/api/v1/medications/MEDICATION_1CODE"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void deleteMedicationNotFound() throws Exception {
        when(medicationService.findById("MEDICATION_1CODE")).thenThrow(new MedicationNotFoundException("MEDICATION_1CODE"));
        this.mockMvc
                .perform(MockMvcRequestBuilders.delete("/api/v1/medications/MEDICATION_1CODE"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}