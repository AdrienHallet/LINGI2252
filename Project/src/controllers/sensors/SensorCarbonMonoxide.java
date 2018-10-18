package controllers.sensors;

public class SensorCarbonMonoxide extends Sensor {

    public SensorCarbonMonoxide(Boolean broadcast){
        super(broadcast);
        this.type = "carbonMonoxide";
    }

    public String getStateAsString() {
        if (this.value > 0.0)
            return "CO detected";
        return "No CO detected";
    }
}
