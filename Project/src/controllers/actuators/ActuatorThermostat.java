package controllers.actuators;

public class ActuatorThermostat extends Actuator {
    public ActuatorThermostat() {
        this.type = "thermostat";
    }
    public String getStateAsString() {
        if (this.isTriggered())
            return "*Heating system is on*";
        return "*Heating system is off*";
    }
}
