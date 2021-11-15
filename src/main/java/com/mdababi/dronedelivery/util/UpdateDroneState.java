package com.mdababi.dronedelivery.util;

import com.mdababi.dronedelivery.model.Drone;
import com.mdababi.dronedelivery.model.DroneState;
import com.mdababi.dronedelivery.services.DroneService;
import com.mdababi.dronedelivery.services.serviceImpl.DroneServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class UpdateDroneState implements Runnable {

    @Autowired
    private DroneService droneService;

    private final long waitTime;
    private final String droneSerialNumber;
    private static final Logger log = LoggerFactory.getLogger(DroneServiceImpl.class);

    public UpdateDroneState(long waitTime, String droneSerialNumber) {
        this.waitTime = waitTime;
        this.droneSerialNumber = droneSerialNumber;
    }

    @Override
    public void run()
    {
        try {
            // sleep for user given millisecond
            // before checking again
            Thread.sleep(waitTime);
            // change drone state to idle
            Drone drone = droneService.findById(droneSerialNumber);
            drone.setState(DroneState.IDLE);
            droneService.saveDrone(drone);
        }
 
        catch (InterruptedException ex) {
            log.error("error changing the drone with serial number {} to idle state", droneSerialNumber);
        }
    }
}
