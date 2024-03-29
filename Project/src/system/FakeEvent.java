package system;

import controllers.sensors.Sensor;

/**
 * Allow the system to simulate fake events. In reality, all those events would
 * exist in the environment and the sensors would pick up the changes.
 *
 * This is therefore just a basic world simulation
 */
public class FakeEvent {

    /**
     * @param housePart where to start a fire
     */
    public static void startFire(HousePart housePart){
        for (Sensor cSensor : housePart.sensors){
            if (cSensor.type.equals(Sensor.SMOKE))
                cSensor.trigger();
        }
    }

    /**
     *
     * @param housePart the housePart you want to set the T
     * @param temperature the temperature in Celsius degrees
     */
    public static void setTemperature(HousePart housePart, double temperature) {
        for (Sensor cSensor : housePart.sensors){
            if (cSensor.type.equals(Sensor.THERMOMETER)){
                cSensor.value = temperature;
            }
        }
    }

    /**
     *
     * @param housePart the housePart you want to set to a certain amount of light
     * @param light the light amount (0=dark, 100.0=sun's surface)
     */
    public static void setLight(HousePart housePart, double light) {
        for (Sensor cSensor : housePart.sensors){
            if (cSensor.type.equals(Sensor.LIGHT)){
                cSensor.value = light;
            }
        }
    }

    public static void detectMotion(HousePart housePart){
        for (Sensor cSensor : housePart.sensors){
            if (cSensor.type.equals(Sensor.MOTION)){
                cSensor.trigger();
            }
        }
    }

    public static void resetMotion(HousePart housePart){
        for (Sensor cSensor : housePart.sensors){
            if (cSensor.type.equals(Sensor.MOTION)){
                cSensor.reset();
            }
        }
    }
}
