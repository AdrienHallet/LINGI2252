import controllers.sensors.Sensor;

public class FakeEvent {

    /**
     * @param room where to start a fire
     */
    public static void startFire(Room room){
        for (Sensor cSensor : room.sensors){
            if (cSensor.type.equals("smoke"))
                cSensor.trigger();
        }
    }

    /**
     *
     * @param room the room you want to set the T
     * @param temperature the temperature in Celsius degrees
     */
    public static void setTemperature(Room room, double temperature) {
        for (Sensor cSensor : room.sensors){
            if (cSensor.type.equals("thermometer")){
                cSensor.value = temperature;
            }
        }
    }

    /**
     *
     * @param room the room you want to set to a certain amount of light
     * @param light the light amount (0=dark, 100.0=sun's surface)
     */
    public static void setLight(Room room, double light) {
        for (Sensor cSensor : room.sensors){
            if (cSensor.type.equals("light")){
                cSensor.value = light;
            }
        }
    }
}