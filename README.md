# Drone delivery
Drone delivery is a Spring boot application. The project's goal is to simulate a drone delivery process.

## Description
Drone delivery is an application which provides the user with a REST API to perform operations on drone, medication items and deliveries. It allow user to register drones, medications and create deliveries.

## Assumptions
### Necessary elements to create a delivery
- The user needs to provide the serial number of the drone and a list of medication items codes.
- The drone must exits in the database and must be available.
- The drone battry level must be above 24%.
- the medication items must exits in the database and their total weight is less than the weight the drone can carry.
### Drone state management
- A drone is initially in the IDLE state.
- Once verification of medication items and drone existence| weight done, The drone state change to the LOADING state.
- After delivery creation, The drone state change to the LOADED state.
- A FutureTask task is created to revert the state to IDLE after delivery finished ( between 1 and 5 minutes)

## Running the Application
Clone the repository
```bash
$ git clone https://github.com/mdababi1989/DroneDelivery.git
```
Check into the cloned repository
```bash
$ cd DroneDelivery
```
Install the dependencies and package the application
```bash
$ mvn package
```
Run the API
```bash
mvn spring-boot:run
```
View the documentation at:
```bash
http://localhost:8080/swagger-ui/
```
## Database
In this app, I used H2 in-memory database. It contains some data creating by the ApplicationBootstrap class.

How to access the database:
- URL : http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:drone_delivery
- username: sa
- password :
## Logging
loging are createed in the /logs folder (drone_delivery.log file).
When a log file reach a size of 10 M, its archived and rolled.
