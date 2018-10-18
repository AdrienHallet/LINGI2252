import controllers.actuators.Actuator;
import controllers.connectedObjects.ConnectedObject;
import controllers.sensors.Sensor;

import java.util.Scanner;

public class HomeController {

    static House myHouse;
    static Scenario scenario;
    static boolean userInputScenario = true;

    public static void main (String[] args){
        myHouse = new House(); // Initialize the house from configuration
        scenario = new Scenario(myHouse);
        scenario.userInput();
    }

    /* Send the events to the controllers around the house */
    static void controller_loop(){
        for (Room cRoom : myHouse.roomList){
            if(cRoom.sensors != null) {
                for (Sensor cSensor : cRoom.sensors) {
                    if (cSensor.isTriggered()) {
                        triggerActions(cSensor);
                    }
                }
            }
        }
        state_loop();
    }

    /**
     * Loop through the house, looking for controllers that do things
     * and display those actions to the user
     * ToDo : move the string creation elsewhere
     */
    static void state_loop(){
        for (Room cRoom : myHouse.roomList){
            if (cRoom.actuators != null) {
                // Display the states of the actuators, per sensor, per room
                for (Sensor sensor : cRoom.sensors) {
                    for (Actuator actuator : sensor.getActuatorList()) {
                        if (actuator.isTriggered()) {
                            System.out.printf("[%s:%s]: %s\n", cRoom.name, actuator.type, actuator.getStateAsString());
                        }
                    }
                }
                // Display the states of the connected objects, per room
            }
            if (cRoom.connectedObjects != null){
                for (ConnectedObject connectedObject : cRoom.connectedObjects){
                    if (connectedObject.isTriggered()){
                        System.out.printf("[%s:%s]: %s\n", cRoom.name, connectedObject.type, connectedObject.getStateAsString());
                    }
                }
            }
        }
        if (userInputScenario)
            scenario.userInput();
    }

    /* Trigger all actions linked to a sensor */
    static void triggerActions(Sensor sensor){
        Actuator[] aList = sensor.getActuatorList();
        for(Actuator cActuator : aList){
            cActuator.trigger();
        }
    }

    static void turnObjectOn(Room room, String type){
        for (ConnectedObject object : room.connectedObjects){
            if(object.type.equals(type)){
                object.enable();
                return;
            }
        }
        System.err.format("No %s in %s", type, room);
    }

    /* Display commands to user */
    static void printHelp(){
        System.out.println("ToDo"); //todo
    }
}
