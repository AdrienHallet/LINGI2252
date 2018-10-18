package controllers.sensors;

public class SensorHumidity extends Sensor {

    public SensorHumidity(Boolean broadcast){
        super(broadcast);
        this.type = "humidity";
    }

    public String getStateAsString(){
        return "Humidity %:" + this.value;
    }
}
