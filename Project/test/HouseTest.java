import controllers.Link;
import controllers.actuators.Actuator;
import controllers.actuators.ActuatorAudioAlarm;
import controllers.actuators.ActuatorMotor;
import controllers.actuators.ActuatorMotorDoor;
import controllers.connectedObjects.ConnectedObject;
import controllers.sensors.Sensor;
import controllers.sensors.SensorMotion;
import controllers.sensors.SensorSmokeDetector;
import org.junit.jupiter.api.Test;
import system.House;
import system.HousePart;

import static org.junit.jupiter.api.Assertions.*;

class HouseTest {
    @org.junit.jupiter.api.Test
    void testEmptyHouse() {
        House house = House.getOrCreate("test/jsons/config-empty.json");

        assertNull(house.getHousePartByName("Entrance Hall"));
    }

    @org.junit.jupiter.api.Test
    void testOnlyRooms() {
        House house = House.getOrCreate("test/jsons/config-noSensors.json");

        String hallName = "Entrance Hall", bedroomName = "Bedroom";

        HousePart hall = house.getHousePartByName(hallName);
        assertNotNull(hall, "'Entrance hall' wasn't correctly parsed.");
        HousePart bedroom = house.getHousePartByName(bedroomName);
        assertNotNull(bedroom, "'Bedroom' wasn't correctly parsed.");

        assertEquals(hall.name, hallName);
        assertEquals(hall.accessibleHouseParts.length, 1);
        assertEquals(hall.accessibleHouseParts[0], bedroomName);
        assertEquals(hall.sensors.length, 0);
        assertEquals(hall.actuators.length, 0);
        assertEquals(hall.connectedObjects.length, 0);

        assertEquals(bedroom.name, bedroomName);
        assertEquals(bedroom.accessibleHouseParts.length, 1);
        assertEquals(bedroom.accessibleHouseParts[0], hallName);
        assertEquals(bedroom.sensors.length, 0);
        assertEquals(bedroom.actuators.length, 0);
        assertEquals(bedroom.connectedObjects.length, 0);
    }

    @org.junit.jupiter.api.Test
    void testSensorActuator() {
        House house = House.getOrCreate("test/jsons/config-onlyHall.json");

        String hallName = "Entrance Hall";

        HousePart hall = house.getHousePartByName(hallName);
        assertNotNull(hall, "'Entrance hall' wasn't correctly parsed.");

        assertEquals(hall.accessibleHouseParts.length, 0);
        assertEquals(hall.sensors.length, 1);
        assertNotNull(hall.sensors[0]);
        assertTrue(hall.sensors[0] instanceof SensorMotion);
        assertFalse(hall.sensors[0].shouldBroadcast());

        Link[] linkedActuators = hall.sensors[0].getActuatorList();
        assertEquals(linkedActuators.length, 1);
        assertNotNull(linkedActuators[0]);
        assertEquals(linkedActuators[0].actuatorType, Actuator.AUDIO);
        assertEquals(linkedActuators[0].housePart, hallName);

        Actuator[] actuators = hall.actuators;
        assertEquals(actuators.length, 1);
        assertNotNull(actuators[0]);
        assertTrue(actuators[0] instanceof ActuatorAudioAlarm);

        assertEquals(hall.connectedObjects.length, 1);
    }

    @Test
    void testFullHouse() {
        House house = House.getOrCreate("test/jsons/config-fullHouse.json");

        String hallName = "Entrance Hall", kitchenName = "Kitchen", bedroomName = "Bedroom";

        // Check "Entrance Hall"
        HousePart hall = house.getHousePartByName(hallName);
        assertNotNull(hall, "'Entrance hall' wasn't correctly parsed.");

        assertEquals(hall.accessibleHouseParts.length, 2);
        assertEquals(hall.sensors.length, 2);
        for (Sensor sensor: hall.sensors)
            assertNotNull(sensor);

        assertTrue(hall.sensors[0] instanceof SensorMotion);
        assertFalse(hall.sensors[0].shouldBroadcast());
        Link[] linkedActuators = hall.sensors[0].getActuatorList();
        assertEquals(linkedActuators.length, 1);
        assertNotNull(linkedActuators[0]);
        assertEquals(linkedActuators[0].actuatorType, Actuator.MOTOR_DOOR);
        assertEquals(linkedActuators[0].housePart, bedroomName);

        assertTrue(hall.sensors[1] instanceof SensorSmokeDetector);
        assertTrue(hall.sensors[1].shouldBroadcast());
        linkedActuators = hall.sensors[1].getActuatorList();
        assertEquals(linkedActuators.length, 1);
        assertNotNull(linkedActuators[0]);
        assertEquals(linkedActuators[0].actuatorType, Actuator.AUDIO);
        assertEquals(linkedActuators[0].housePart, hallName);

        assertEquals(hall.actuators.length, 2);
        for (Actuator actuator: hall.actuators)
            assertNotNull(actuator);
        assertTrue(hall.actuators[0] instanceof ActuatorAudioAlarm);
        assertTrue(hall.actuators[1] instanceof ActuatorMotorDoor);

        assertEquals(hall.connectedObjects.length, 1);

        // Check "Kitchen"
        HousePart kitchen = house.getHousePartByName(kitchenName);
        assertNotNull(kitchen, "'Kitchen' wasn't correctly parsed");

        assertEquals(kitchen.accessibleHouseParts.length, 1);
        assertEquals(kitchen.sensors.length, 1);
        assertTrue(kitchen.sensors[0] instanceof SensorSmokeDetector);
        assertTrue(kitchen.sensors[0].shouldBroadcast());
        linkedActuators = kitchen.sensors[0].getActuatorList();
        assertEquals(linkedActuators.length, 1);
        assertNotNull(linkedActuators[0]);
        assertEquals(linkedActuators[0].actuatorType, Actuator.AUDIO);
        assertEquals(linkedActuators[0].housePart, kitchenName);

        assertEquals(kitchen.actuators.length, 1);
        assertTrue(kitchen.actuators[0] instanceof ActuatorAudioAlarm);

        // Check "Bedroom"
        HousePart bedroom = house.getHousePartByName(bedroomName);
        assertNotNull(bedroom, "'Bedroom' wasn't correctly parsed");

        assertEquals(bedroom.accessibleHouseParts.length, 1);
        assertEquals(bedroom.sensors.length, 0);

        assertEquals(bedroom.actuators.length, 1);
        assertTrue(bedroom.actuators[0] instanceof ActuatorAudioAlarm);
    }
}