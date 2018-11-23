import controllers.Link;
import controllers.UnexistantControllerException;
import controllers.actuators.*;
import controllers.connectedObjects.ConnectedObject;
import controllers.connectedObjects.ConnectedObjectFactory;
import controllers.connectedObjects.ConnectedObjectNull;
import controllers.connectedObjects.ConnectedRadio;
import controllers.sensors.*;
import org.junit.jupiter.api.Test;
import system.House;
import system.HousePart;

import static org.junit.jupiter.api.Assertions.*;

class HouseTest {
    @Test
    void testEmptyHouse() {
        House house = House.getOrCreate("test/jsons/config-empty.json");

        assertNull(house.getHousePartByName("Entrance Hall"));
    }

    @Test
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

    @Test
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

        Actuator[] linkedActuators = hall.sensors[0].getActuatorList();
        assertEquals(linkedActuators.length, 1);
        assertNotNull(linkedActuators[0]);
        assertEquals(linkedActuators[0].type, Actuator.AUDIO);

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
        Actuator[] linkedActuators = hall.sensors[0].getActuatorList();
        assertEquals(linkedActuators.length, 1);
        assertNotNull(linkedActuators[0]);
        assertEquals(linkedActuators[0].type, Actuator.MOTOR_DOOR);
//        assertEquals(linkedActuators[0].housePart, bedroomName);

        assertTrue(hall.sensors[1] instanceof SensorSmokeDetector);
        assertTrue(hall.sensors[1].shouldBroadcast());
        linkedActuators = hall.sensors[1].getActuatorList();
        assertEquals(linkedActuators.length, 1);
        assertNotNull(linkedActuators[0]);
        assertEquals(linkedActuators[0].type, Actuator.AUDIO);
//        assertEquals(linkedActuators[0].housePart, hallName);

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
        assertEquals(linkedActuators[0].type, Actuator.AUDIO);

        assertEquals(kitchen.actuators.length, 1);
        assertTrue(kitchen.actuators[0] instanceof ActuatorAudioAlarm);

        // Check "Bedroom"
        HousePart bedroom = house.getHousePartByName(bedroomName);
        assertNotNull(bedroom, "'Bedroom' wasn't correctly parsed");

        assertEquals(bedroom.accessibleHouseParts.length, 1);
        assertEquals(bedroom.sensors.length, 0);

        assertEquals(bedroom.actuators.length, 2);
        assertTrue(bedroom.actuators[0] instanceof ActuatorAudioAlarm);
    }

    @Test
    void testSensorFactory() {
        try {
            assertTrue(SensorFactory.create(Sensor.AUDIO, false) instanceof SensorAudio);
            assertTrue(SensorFactory.create(Sensor.BADGE, false) instanceof SensorBadgeDetector);
            assertTrue(SensorFactory.create(Sensor.CAMERA, false) instanceof SensorCamera);
            assertTrue(SensorFactory.create(Sensor.CARBON_MONOXIDE, false) instanceof SensorCarbonMonoxide);
            assertTrue(SensorFactory.create(Sensor.CLOCK, false) instanceof SensorClock);
            assertTrue(SensorFactory.create(Sensor.CONSUMPTION, false) instanceof SensorConsumption);
            assertTrue(SensorFactory.create(Sensor.HUMIDITY, false) instanceof SensorHumidity);
            assertTrue(SensorFactory.create(Sensor.LIGHT, false) instanceof SensorLight);
            assertTrue(SensorFactory.create(Sensor.MOTION, false) instanceof SensorMotion);
            assertTrue(SensorFactory.create(Sensor.PROXIMITY, false) instanceof SensorProximity);
            assertTrue(SensorFactory.create(Sensor.SMOKE, false) instanceof SensorSmokeDetector);
            assertTrue(SensorFactory.create(Sensor.THERMOMETER, false) instanceof SensorThermometer);

            Sensor sensorNoBroadcast = SensorFactory.create(Sensor.AUDIO, false);
            assertFalse(sensorNoBroadcast.shouldBroadcast());
            Sensor sensorBroadcast = SensorFactory.create(Sensor.AUDIO, true);
            assertTrue(sensorBroadcast.shouldBroadcast());

        } catch (UnexistantControllerException e) {
            fail("UnexistantControllerException");
        }

        try {
            assertTrue(SensorFactory.create("invalid", false) instanceof SensorNull);

        } catch (UnexistantControllerException ignored) {}
    }

    @Test
    void testActuatorFactory() {
        try {
            assertTrue(ActuatorFactory.create(Actuator.AUDIO) instanceof ActuatorAudioAlarm);
            assertTrue(ActuatorFactory.create(Actuator.LIGHT) instanceof ActuatorLightbulb);
            assertTrue(ActuatorFactory.create(Actuator.LOCK) instanceof ActuatorLock);
            assertTrue(ActuatorFactory.create(Actuator.MOTOR) instanceof ActuatorMotor);
            assertTrue(ActuatorFactory.create(Actuator.MOTOR_CURTAINS) instanceof ActuatorMotorCurtains);
            assertTrue(ActuatorFactory.create(Actuator.MOTOR_DOOR) instanceof ActuatorMotorDoor);
            assertTrue(ActuatorFactory.create(Actuator.THERMOSTAT) instanceof ActuatorThermostat);
        } catch (UnexistantControllerException e) {
            fail("UnexistantControllerException");
        }

        try {
            assertTrue(ActuatorFactory.create("invalid") instanceof ActuatorNull);
        } catch (UnexistantControllerException ignored) {}
    }

    @Test
    void testConnectedObjectFactory() {
        try {
            assertTrue(ConnectedObjectFactory.create(ConnectedObject.RADIO) instanceof ConnectedRadio);
        } catch (UnexistantControllerException e) {
            fail("UnexistantControllerException");
        }

        try {
            assertTrue(ConnectedObjectFactory.create("invalid") instanceof ConnectedObjectNull);
        } catch (UnexistantControllerException ignored) {}
    }
}