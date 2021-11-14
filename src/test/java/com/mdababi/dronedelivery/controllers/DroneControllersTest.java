package com.mdababi.dronedelivery.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdababi.dronedelivery.exceptions.DroneNotFoundException;
import com.mdababi.dronedelivery.exceptions.NoDataFoundException;
import com.mdababi.dronedelivery.model.*;
import com.mdababi.dronedelivery.services.DroneService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
class DroneControllerTest {
    @MockBean
    private DroneService droneService;
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Drone drone1, drone2, drone3, invalidDrone;
    List<Drone> droneList;

    @BeforeEach
    void setUp() {
        drone1 = new Drone("drone1SerialNumber", DroneModel.Cruiserweight, 300, 100, DroneState.IDLE, null);
        drone2 = new Drone("drone2SerialNumber", DroneModel.Heavyweight, 400, 70, DroneState.IDLE, null);
        drone3 = new Drone("drone3SerialNumber", DroneModel.Lightweight, 200, 60, DroneState.IDLE, null);
        // wightLimit = 900 > max 500 and battery = 150 > max 100
        invalidDrone = new Drone("invalidDroneSerialNumber", DroneModel.Lightweight, 900, 150, DroneState.IDLE, null);
        droneList = new ArrayList<>(Arrays.asList(drone1, drone2, drone3));
    }


    @Test
    void getDroneListNotEmpty() throws Exception {
        when(droneService.getDroneList()).thenReturn(droneList);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/api/v1/drones"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$[*]", hasSize(3)))
                .andExpect(jsonPath("$[0].serialNumber").value("drone1SerialNumber"))
                .andExpect(jsonPath("$[0].weightLimit").value(300))
                .andExpect(jsonPath("$[1].model").value("Heavyweight"))
                .andExpect(jsonPath("$[2].state").value("IDLE"));
    }

    @Test
    void getDroneListEmpty() throws Exception {
        when(droneService.getDroneList()).thenThrow(new NoDataFoundException("Drone"));
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/api/v1/drones"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void findByIdOk() throws Exception {
        when(droneService.findById("drone1SerialNumber")).thenReturn(drone1);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/api/v1/drones/drone1SerialNumber"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.serialNumber").value("drone1SerialNumber"))
                .andExpect(jsonPath("$.weightLimit").value(300))
                .andExpect(jsonPath("$.batteryCapacity").value(100));

    }

    @Test
    void findByIdNotFound() throws Exception {
        when(droneService.findById("drone1SerialNumber")).thenThrow(new DroneNotFoundException("drone1SerialNumber"));
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/api/v1/drones/drone1SerialNumber"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    void saveValidDrone_OkResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/drones/add")
                .content(objectMapper.writeValueAsString(drone1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void saveInvalidDrone_BadResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/drones/add")
                .content(objectMapper.writeValueAsString(invalidDrone))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect( jsonPath("$.weightLimit").value("Weight Limit should not be greater than 500 ( rejected value: 900 )"))
                .andExpect( jsonPath("$.weightLimit").value("Weight Limit should not be greater than 500 ( rejected value: 900 )"));
    }

    @Test
    void deleteDroneOk() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.delete("/api/v1/drones/drone1SerialNumber"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void deleteDroneNotFound() throws Exception {
        when(droneService.findById("drone1SerialNumber")).thenThrow(new DroneNotFoundException("drone1SerialNumber"));
        this.mockMvc
                .perform(MockMvcRequestBuilders.delete("/api/v1/drones/drone1SerialNumber"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void loadedMedications() throws Exception {
        when(droneService.loadedMedications("drone1SerialNumber")).thenReturn(Arrays.asList(new Medication(), new Medication()));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/api/v1/drones/getMedications/drone1SerialNumber"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$[*]", hasSize(2)));
    }

    @Test
    void getBatteryLevel() throws Exception {
        when(droneService.findById("drone2SerialNumber")).thenReturn(drone2);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/api/v1/drones/getBatteryLevel/drone2SerialNumber"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getAvailableDrones() throws Exception {
        when(droneService.getAvailableDrones()).thenReturn(Arrays.asList(drone1, drone2));
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/api/v1/drones/availables"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$[*]", hasSize(2)))
                .andExpect(jsonPath("$[0].serialNumber").value("drone1SerialNumber"))
                .andExpect(jsonPath("$[1].serialNumber").value("drone2SerialNumber"));
    }
}