package controllers.sensors;

import controllers.Controller;
import controllers.actuators.Actuator;

public abstract class Sensor extends Controller {
    boolean broadcast = false;
    Actuator[] actuatorList;

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
