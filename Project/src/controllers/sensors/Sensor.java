package controllers.sensors;

import controllers.Controller;
import controllers.Link;
import controllers.actuators.Actuator;
import system.House;
import system.HousePart;

import java.util.Scanner;

public abstract class Sensor extends Controller {

    public static final String AUDIO = "audio";
    public static final String BADGE = "badge-detector";
    public static final String CAMERA = "camera";
    public static final String CARBON_MONOXIDE = "carbonMonoxide";
    public static final String CLOCK = "clock";
    public static final String CONSUMPTION = "consumption";
    public static final String HUMIDITY = "humidity";
    public static final String LIGHT = "light";
    public static final String MOTION = "motion";
    public static final String PROXIMITY = "proximity";
    public static final String SMOKE = "smoke";
    public static final String THERMOMETER = "thermometer";

    boolean broadcast = false;
    Link[] actuatorList;

    public Sensor(){
        this.broadcast = false;
    }

    public Sensor(Boolean broadcast){
        this.broadcast = broadcast;
    }

    /**
     * When a sensor is reset, reset the linked actuators
     */
    public void reset(House myHouse){
        this.value = 0.0;
        for (Link actuator : actuatorList){
            HousePart cHousePart = myHouse.getHousePartByName(actuator.housePart);
            for (Actuator cActuator : cHousePart.actuators){
                if (cActuator.type.equalsIgnoreCase(actuator.actuatorType)){
                    cActuator.reset();
                }
            }
        }
    }

    public boolean shouldBroadcast(){
        return this.broadcast;
    }
    public void setActuatorList(Link[] list){
        this.actuatorList = list;
    }

    public Link[] getActuatorList(){
        return this.actuatorList;
    }
}
