package controllers.actuators;

public class ActuatorLock extends Actuator {
    public ActuatorLock() {
        this.type = LOCK;
    }
    public String getStateAsString() {
        if (this.isTriggered())
            return "Locks are unlocked";
        return "Locks are locked";
    }
}
