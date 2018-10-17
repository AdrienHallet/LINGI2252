package controllers.sensors;

import controllers.Controller;
import controllers.actuators.Actuator;

public abstract class Sensor extends Controller {
    boolean broadcast = false;
    Actuator[] actuatorList;

    @Override
    /*
     * When a sensor is reset, reset the linked actuators
     */
    public void reset(){
        this.value = 0.0;
        for (Actuator actuator : actuatorList){
            actuator.reset();
        }
    }

    public Sensor(Boolean broadcast){
        this.broadcast = broadcast;
    }

    public void setActuatorList(Actuator[] list){
        this.actuatorList = list;
    }

    public Actuator[] getActuatorList(){
        return this.actuatorList;
    }
}
