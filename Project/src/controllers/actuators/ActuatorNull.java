package controllers.actuators;

public class ActuatorNull extends Actuator {

    public ActuatorNull(){
        this.type = "null";
    }

    public String getStateAsString(){
        return "I am not a correct actuator";
    }
}
