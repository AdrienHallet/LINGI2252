package controllers.sensors;

public class SensorMotion extends Sensor {

    public SensorMotion(boolean broadcast){
        super(broadcast);
        this.type = MOTION;
    }

    public String getStateAsString(){
        if (value > 0.0)
            return "SOMETHING IS MOVING";
        else
            return "No movement";
    }
}
