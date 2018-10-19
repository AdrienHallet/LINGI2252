package controllers.actuators;

public class ActuatorMotorCurtains extends ActuatorMotor {

    public ActuatorMotorCurtains(){
        this.type = "motorCurtains";
    }

    public String getStateAsString(){
        if (this.value > 0.0)
            return "The curtains are open";
        return "The curtains are closed";
    }
}
