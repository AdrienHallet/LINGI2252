package controllers.sensors;

import controllers.Controller;
import controllers.actuators.Actuator;

public abstract class Sensor extends Controller {
    boolean broadcast = false;
    Actuator[] actuatorList;

    public Sensor(){
        this.broadcast = false;
    }

    public Sensor(Boolean broadcast){
        this.broadcast = broadcast;
    }

    /**
     * When a sensor is reset, reset the linked actuators
     */
    public void reset(){
        this.value = 0.0;
        for (Actuator actuator : actuatorList){
            actuator.reset();
        }
    }

    public boolean shouldBroadcast(){
        return this.broadcast;
    }
    public void setActuatorList(Actuator[] list){
        this.actuatorList = list;
    }

    public Actuator[] getActuatorList(){
        return this.actuatorList;
    }
}
