package controllers.actuators;

import controllers.UnexistantControllerException;

public class ActuatorFactory {

    public static Actuator create(String type) throws UnexistantControllerException{
        switch (type){
            case Actuator.AUDIO:
                return new ActuatorAudioAlarm();
            case Actuator.LIGHT:
                return new ActuatorLightbulb();
            case Actuator.LOCK:
                return new ActuatorLock();
            case Actuator.MOTOR:
                return new ActuatorMotor();
            case Actuator.MOTOR_CURTAINS:
                return new ActuatorMotorCurtains();
            case Actuator.MOTOR_DOOR:
                return new ActuatorMotorDoor();
            case Actuator.THERMOSTAT:
                return new ActuatorThermostat();
            default:
                throw new UnexistantControllerException(type);
        }
    }
}
