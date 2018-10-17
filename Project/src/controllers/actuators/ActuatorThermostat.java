package controllers.actuators;

public class ActuatorThermostat extends Actuator {
    public ActuatorThermostat() {
        this.type = "thermostat";
    }
    public String getStateAsString() {
        if (this.isTriggered())
            return "*Thermostat has launched the heating system*";
        return "*Thermostat has stopped the heating system*";
    }
}
