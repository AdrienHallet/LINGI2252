package controllers.actuators;

public class ActuatorMotorDoor extends ActuatorMotor {

    public ActuatorMotorDoor(){
        this.type = MOTOR_DOOR;
    }
    public String getStateAsString(){
        if (this.value > 0.0)
            return "The door is open";
        return "The door is closed";
    }
}
