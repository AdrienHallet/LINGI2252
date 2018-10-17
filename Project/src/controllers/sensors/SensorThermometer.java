package controllers.sensors;

public class SensorThermometer extends Sensor {

    public double triggerTemperature = 22.0; // Default value when a thermometer triggers the signal to the thermostat

    public SensorThermometer(boolean broadcast, double triggerTemperature){
        super(broadcast);
        this.type = "thermometer";
        this.triggerTemperature = triggerTemperature;
        this.value = triggerTemperature;
    }

    @Override
    public boolean isTriggered(){
        if (this.value < triggerTemperature)
            return true;
        return false;
    }

    @Override
    public void reset(){
        this.value = this.triggerTemperature;
    }

    public String getStateAsString(){
       return String.format("There is about %d °C", this.value);
    }
}
