package controllers.sensors;

import controllers.sensors.Sensor;

public class SensorNull extends Sensor {
    public SensorNull(){
        type = "null";
    }

    public String getStateAsString() {
        return "I am not a correct sensor";
    }
}
