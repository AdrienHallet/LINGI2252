import controllers.actuators.Actuator;
import controllers.actuators.ActuatorAudioAlarm;
import controllers.sensors.SensorMotion;

import static org.junit.jupiter.api.Assertions.*;

class HouseTest {
    @org.junit.jupiter.api.Test
    void testEmptyHouse() {
        House house = new House("test/jsons/config0.json");

        assertNull(house.getRoomByName("Entrance Hall"));
    }

    @org.junit.jupiter.api.Test
    void testOnlyRooms() {
        House house = new House("test/jsons/config1.json");

        String hallName = "Entrance Hall", bedroomName = "Bedroom";

        Room hall = house.getRoomByName(hallName);
        assertNotNull(hall, "'Entrance hall' wasn't correctly parsed.");
        Room bedroom = house.getRoomByName(bedroomName);
        assertNotNull(bedroom, "'Bedroom' wasn't correctly parsed.");

        assertEquals(hall.name, hallName);
        assertEquals(hall.accessibleRooms.length, 1);
        assertEquals(hall.accessibleRooms[0], bedroomName);
        assertEquals(hall.sensors.length, 0);
        assertEquals(hall.actuators.length, 0);
        assertEquals(hall.connectedObjects.length, 0);

        assertEquals(bedroom.name, bedroomName);
        assertEquals(bedroom.accessibleRooms.length, 1);
        assertEquals(bedroom.accessibleRooms[0], hallName);
        assertEquals(bedroom.sensors.length, 0);
        assertEquals(bedroom.actuators.length, 0);
        assertEquals(bedroom.connectedObjects.length, 0);
    }

    @org.junit.jupiter.api.Test
    void testSensorActuator() {
        House house = new House("test/jsons/config2.json");

        String hallName = "Entrance Hall";

        Room hall = house.getRoomByName(hallName);
        assertNotNull(hall, "'Entrance hall' wasn't correctly parsed.");

        assertEquals(hall.accessibleRooms.length, 0);
        assertEquals(hall.sensors.length, 1);
        assertTrue(hall.sensors[0] instanceof SensorMotion);

        Actuator[] linkedActuators = hall.sensors[0].getActuatorList();
        assertEquals(linkedActuators.length, 1);
        assertTrue(linkedActuators[0] instanceof ActuatorAudioAlarm);
    }
}