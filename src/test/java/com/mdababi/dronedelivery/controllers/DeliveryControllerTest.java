package com.mdababi.dronedelivery.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdababi.dronedelivery.exceptions.DroneNotFoundException;
import com.mdababi.dronedelivery.exceptions.MedicationNotFoundException;
import com.mdababi.dronedelivery.model.*;
import com.mdababi.dronedelivery.services.DeliveryService;
import com.mdababi.dronedelivery.services.DroneService;
import com.mdababi.dronedelivery.services.MedicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DeliveryControllerTest {
    @MockBean
    private DeliveryService deliveryService;
    @MockBean
    private MedicationService medicationService;
    @MockBean
    private DroneService droneService;
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Drone drone1, drone2, drone3;
    private Medication medication1, medication2, medication3;
    List<Medication> medicationList;
    List<String> medicationListCodes;



    @BeforeEach
    void setUp() {
        // Drone used for updated with Http status ok
        drone1 = new Drone("drone1SerialNumber", DroneModel.Heavyweight, 500, 100, DroneState.IDLE, null);
        // Drone used for updated with low battery exception
        drone2 = new Drone("drone2SerialNumber", DroneModel.Heavyweight, 400, 10, DroneState.IDLE, null);
        // Drone used for updated with weight exception
        drone3 = new Drone("drone3SerialNumber", DroneModel.Lightweight, 200, 60, DroneState.IDLE, null);
        // medications used for tests
        medication1 = new Medication("MEDICATION_1CODE", "medication1", 140);
        medication2 = new Medication("MEDICATION2_CODE", "medication2", 105);
        medication3 = new Medication("MEDICATION3_CODE", "medication3", 50);
        medicationList = new ArrayList<>(Arrays.asList(medication1, medication2, medication3));
        medicationListCodes = medicationList.stream().map(Medication::getCode).collect(Collectors.toList());

        when(droneService.findById(drone1.getSerialNumber())).thenReturn(drone1);
        when(droneService.findById(drone2.getSerialNumber())).thenReturn(drone2);
        when(droneService.findById(drone3.getSerialNumber())).thenReturn(drone3);

        when(medicationService.findById(medication1.getCode())).thenReturn(medication1);
        when(medicationService.findById(medication2.getCode())).thenReturn(medication2);
        when(medicationService.findById(medication3.getCode())).thenReturn(medication3);
        // drone and medication used for testing non existence
        when(droneService.findById("Inexistent")).thenThrow(new DroneNotFoundException("Inexistent"));
        when(medicationService.findById("Inexistent")).thenThrow(new MedicationNotFoundException("Inexistent"));

    }

    @Test
    void addDeliveryOk() throws Exception {
        DeliveryDto deliveryDto = new DeliveryDto(drone1.getSerialNumber(), medicationListCodes);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/deliveries/add")
                .content(objectMapper.writeValueAsString(deliveryDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

}