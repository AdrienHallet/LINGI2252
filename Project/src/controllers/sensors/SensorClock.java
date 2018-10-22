package controllers.sensors;

public class SensorClock extends Sensor {

    public SensorClock(Boolean broadcast){
        super(broadcast);
        this.type = CLOCK;
    }

    long value;

    public String getStateAsString(){
        return Long.toString(value);
    }

    public void enable(){
        this.value = System.currentTimeMillis();
    }

    public void disable(){
        this.value = 0L;
    }
}
