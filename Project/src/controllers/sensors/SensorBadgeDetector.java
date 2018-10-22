package controllers.sensors;

public class SensorBadgeDetector extends Sensor {

    public SensorBadgeDetector(Boolean broadcast){
        super(broadcast);
        this.type = BADGE;
    }
    public String getStateAsString(){
        if (value > 0.0)
            return "SOMETHING IS MOVING";
        else
            return "No movement";
    }
}
