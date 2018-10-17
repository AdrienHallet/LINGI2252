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

    public static void setTemperature(Room room, double temperature) {
        for (Sensor cSensor : room.sensors){
            if (cSensor.type.equals("thermometer")){
                cSensor.value = temperature;
            }
        }
    }
}
