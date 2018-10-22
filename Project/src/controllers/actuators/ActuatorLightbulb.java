package controllers.actuators;

public class ActuatorLightbulb extends Actuator {
    public ActuatorLightbulb() {
        this.type = LIGHT;
    }

    public String getStateAsString() {
        if (this.isTriggered()) {
            String format = String.format("The light is turned on %1$,.0f%%", value);
            return format;
        }
        return "Let it be darkness";
    }
}
