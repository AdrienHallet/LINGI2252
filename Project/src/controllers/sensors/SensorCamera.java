package controllers.sensors;

public class SensorCamera extends Sensor{

    public SensorCamera(Boolean broadcast){
        super(broadcast);
        this.type = "camera";
    }
    public String getStateAsString() {
        return "Yup, I'm filming";
    }
}
