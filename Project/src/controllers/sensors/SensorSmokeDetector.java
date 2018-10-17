package controllers.sensors;

public class SensorSmokeDetector extends Sensor {

    public SensorSmokeDetector(boolean broadcast){
        super(broadcast);
        this.type = "smoke";
    }

    public String getStateAsString(){
        if (this.value > 0.0)
            return "I sense smoke";
        else
            return "All clear";
    }
}
