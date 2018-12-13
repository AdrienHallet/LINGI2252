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
import system.parametrization.BadConfigException;

import static org.junit.jupiter.api.Assertions.*;

class HouseTest {
    @Test
    void testEmptyHouse() {
        try {
            House house = House.getOrCreate("test/jsons/config-empty.json");

            assertNull(house.getHousePartByName("Entrance Hall"));
        } catch (BadConfigException e) {
            // should miss the smoke detector
            //System.err.println("Invalid configuration: "+e.getMessage());
        }
    }

    @Test
    void testOnlyRooms() {
        try {
            House house = House.getOrCreate("test/jsons/config-noSensors.json");

            String hallName = "Entrance Hall", bedroomName = "Bedroom";

            HousePart hall = house.getHousePartByName(hallName);
            assertNotNull(hall, "'Entrance hall' wasn't correctly parsed.");
            HousePart bedroom = house.getHousePartByName(bedroomName);
            assertNotNull(bedroom, "'Bedroom' wasn't correctly parsed.");

            assertEquals(hallName, hall.name);
            assertEquals(1, hall.accessibleHouseParts.length);
            assertEquals(bedroomName, hall.accessibleHouseParts[0]);
            assertEquals(1, hall.sensors.length);
            assertEquals(1, hall.actuators.length);
            assertEquals(0, hall.connectedObjects.length);

            assertEquals(bedroomName, bedroom.name);
            assertEquals(1, bedroom.accessibleHouseParts.length);
            assertEquals(hallName, bedroom.accessibleHouseParts[0]);
            assertEquals(0, bedroom.sensors.length);
            assertEquals(0, bedroom.actuators.length);
            assertEquals(0, bedroom.connectedObjects.length);
        } catch (BadConfigException e) {
            fail("Invalid configuration: "+e.getMessage());
        }
    }

    @Test
    void testSensorActuator() {
        try {
            House house = House.getOrCreate("test/jsons/config-onlyHall.json");

            String hallName = "Entrance Hall";

            HousePart hall = house.getHousePartByName(hallName);
            assertNotNull(hall, "'Entrance hall' wasn't correctly parsed.");

            assertEquals(0, hall.accessibleHouseParts.length);
            assertEquals(2, hall.sensors.length);
            assertNotNull(hall.sensors[0]);
            assertTrue(hall.sensors[0] instanceof SensorSmokeDetector);
            assertTrue(hall.sensors[0].shouldBroadcast());
            assertNotNull(hall.sensors[1]);
            assertTrue(hall.sensors[1] instanceof SensorMotion);
            assertFalse(hall.sensors[1].shouldBroadcast());

            Actuator[] linkedActuators = hall.sensors[1].getActuatorList();
            assertEquals(1, linkedActuators.length);
            assertNotNull(linkedActuators[0]);
            assertEquals(linkedActuators[0].type, Actuator.AUDIO);

            Actuator[] actuators = hall.actuators;
            assertEquals(1, actuators.length);
            assertNotNull(actuators[0]);
            assertTrue(actuators[0] instanceof ActuatorAudioAlarm);

            assertEquals(1, hall.connectedObjects.length);
        } catch (BadConfigException e) {
            fail("Invalid configuration: "+e.getMessage());
        }
    }

    @Test
    void testFullHouse() {
        try {
            House house = House.getOrCreate("test/jsons/config-fullHouse.json");

            String hallName = "Entrance Hall", kitchenName = "Kitchen", bedroomName = "Bedroom";

            // Check "Entrance Hall"
            HousePart hall = house.getHousePartByName(hallName);
            assertNotNull(hall, "'Entrance hall' wasn't correctly parsed.");

            assertEquals(2, hall.accessibleHouseParts.length);
            assertEquals(2, hall.sensors.length);
            for (Sensor sensor: hall.sensors)
                assertNotNull(sensor);

            assertTrue(hall.sensors[0] instanceof SensorMotion);
            assertFalse(hall.sensors[0].shouldBroadcast());
            Actuator[] linkedActuators = hall.sensors[0].getActuatorList();
            assertEquals(1, linkedActuators.length);
            assertNotNull(linkedActuators[0]);
            assertEquals(linkedActuators[0].type, Actuator.MOTOR_DOOR);
    //        assertEquals(linkedActuators[0].housePart, bedroomName);

            assertTrue(hall.sensors[1] instanceof SensorSmokeDetector);
            assertTrue(hall.sensors[1].shouldBroadcast());
            linkedActuators = hall.sensors[1].getActuatorList();
            assertEquals(1, linkedActuators.length);
            assertNotNull(linkedActuators[0]);
            assertEquals(linkedActuators[0].type, Actuator.AUDIO);
    //        assertEquals(linkedActuators[0].housePart, hallName);

            assertEquals(2, hall.actuators.length);
            for (Actuator actuator: hall.actuators)
                assertNotNull(actuator);
            assertTrue(hall.actuators[0] instanceof ActuatorAudioAlarm);
            assertTrue(hall.actuators[1] instanceof ActuatorMotorDoor);

            assertEquals(1, hall.connectedObjects.length);

            // Check "Kitchen"
            HousePart kitchen = house.getHousePartByName(kitchenName);
            assertNotNull(kitchen, "'Kitchen' wasn't correctly parsed");

            assertEquals(1, kitchen.accessibleHouseParts.length);
            assertEquals(1, kitchen.sensors.length);
            assertTrue(kitchen.sensors[0] instanceof SensorSmokeDetector);
            assertTrue(kitchen.sensors[0].shouldBroadcast());
            linkedActuators = kitchen.sensors[0].getActuatorList();
            assertEquals(1, linkedActuators.length);
            assertNotNull(linkedActuators[0]);
            assertEquals(linkedActuators[0].type, Actuator.AUDIO);

            assertEquals(1, kitchen.actuators.length);
            assertTrue(kitchen.actuators[0] instanceof ActuatorAudioAlarm);

            // Check "Bedroom"
            HousePart bedroom = house.getHousePartByName(bedroomName);
            assertNotNull(bedroom, "'Bedroom' wasn't correctly parsed");

            assertEquals(1, bedroom.accessibleHouseParts.length);
            assertEquals(0, bedroom.sensors.length);

            assertEquals(2, bedroom.actuators.length);
            assertTrue(bedroom.actuators[0] instanceof ActuatorAudioAlarm);
        } catch (BadConfigException e) {
            fail("Invalid configuration: "+e.getMessage());
        }
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

    @Test
    void testBadConfig() {
        try {
            House.getOrCreate("test/jsons/badconfig.json");
            fail("Should have thrown 'BadConfigException'");
        } catch (BadConfigException e) {}
    }

    @Test
    void testMissingActuator() {
        try {
            House.getOrCreate("test/jsons/badconfig-missingActuator.json");
            fail("Should have thrown 'BadConfigException'");
        } catch (BadConfigException e) {}
    }
}