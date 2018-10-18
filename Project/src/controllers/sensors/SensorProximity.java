package controllers.sensors;

public class SensorProximity extends SensorMotion {

    public SensorProximity(Boolean broadcast){
        super(broadcast);
        this.type = "proxitimity";
    }
}
