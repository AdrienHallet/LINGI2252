package controllers.sensors;

import controllers.UnexistantControllerException;

public class SensorFactory {

    public static Sensor create(String type, boolean broadcast) throws UnexistantControllerException{
        switch (type){
            case Sensor.AUDIO:
                return new SensorAudio(broadcast);
            case Sensor.BADGE:
                return new SensorBadgeDetector(broadcast);
            case Sensor.CAMERA:
                return new SensorCamera(broadcast);
            case Sensor.CARBON_MONOXIDE:
                return new SensorCarbonMonoxide(broadcast);
            case Sensor.CLOCK:
                return new SensorClock(broadcast);
            case Sensor.CONSUMPTION:
                return new SensorConsumption(broadcast);
            case Sensor.HUMIDITY:
                return new SensorHumidity(broadcast);
            case Sensor.LIGHT:
                return new SensorLight(broadcast);
            case Sensor.MOTION:
                return new SensorMotion(broadcast);
            case Sensor.PROXIMITY:
                return new SensorProximity(broadcast);
            case Sensor.SMOKE:
                return new SensorSmokeDetector(broadcast);
            case Sensor.THERMOMETER:
                return new SensorThermometer(broadcast, 24.0);
            default:
                return new SensorNull();

        }
    }
}
