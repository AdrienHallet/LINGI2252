package controllers.actuators;

public class ActuatorLightbulb extends Actuator {
    public ActuatorLightbulb() {
        this.type = "light-bulb";
    }

    public String getStateAsString() {
        if (this.isTriggered())
            return "*Let it be light*";
        return "*Let it be darkness*";
    }
}
