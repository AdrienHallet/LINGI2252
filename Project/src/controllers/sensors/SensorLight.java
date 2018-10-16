package controllers.sensors;

public class SensorLight extends Sensor {

    public SensorLight(){
        this.type = "light";
    }

    public String getStateAsString() {
        return String.format("There is about %d percent(s) of light", value);
    }
}
