package controllers.sensors;

public class SensorLight extends Sensor {

    public SensorLight(boolean broadcast){
        super(broadcast);
        this.type = LIGHT;
    }

    public String getStateAsString() {
        return String.format("There is about %d percent(s) of light", value);
    }
}
