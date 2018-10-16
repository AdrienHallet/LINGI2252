package controllers.sensors;

public class SensorMotion extends Sensor {

    public SensorMotion(){
        this.type = "motion";
    }

    public String getStateAsString(){
        if (value > 0.0)
            return "SOMETHING IS MOVING";
        else
            return "No movement";

    }
}
