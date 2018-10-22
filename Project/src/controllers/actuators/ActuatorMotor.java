package controllers.actuators;

public class ActuatorMotor extends Actuator {
    public ActuatorMotor() {
        this.type = MOTOR;
    }

    public String getStateAsString() {
        if (this.isTriggered())
            return "Motor is activating";
        return "Motor is idle";
    }
}
