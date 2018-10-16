package controllers.sensors;

public class SensorSmokeDetector extends Sensor {

    public SensorSmokeDetector(){
        this.type = "smoke-detector";
    }

    public String getStateAsString(){
        if (this.value > 0.0)
            return "I sense smoke";
        else
            return "All clear";
    }
}
