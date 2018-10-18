import controllers.actuators.Actuator;
import controllers.connectedObjects.ConnectedObject;
import controllers.sensors.Sensor;

import java.util.Scanner;

public class HomeController {

    static House myHouse;
    static Scenario scenario;
    static boolean userInputScenario = true;

    /**
     * Launch the system
     * @param args the optional file to read the scenario from
     */
    public static void main (String[] args){
        myHouse = new House(); // Initialize the house from configuration

        if (args.length == 0) {
            scenario = new Scenario(myHouse);
            scenario.userInput();
        }
        else if (args.length == 1) {
            userInputScenario = false;
            scenario = new Scenario(myHouse, args[0]);
            scenario.fileInput();
        }
    }

    /**
     * Tell the sensors to dispatch their triggers to the assigned actuators
     */
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
        else
            scenario.fileInput();
    }

    /**
     * Trigger all the actuators linked to a sensor
     * @param sensor the sensor to launch the event from
     */
    static void triggerActions(Sensor sensor){
        Actuator[] aList = sensor.getActuatorList();
        for(Actuator cActuator : aList){
            cActuator.trigger();
        }
    }

    /**
     * Turn a connected object on
     * @param room the room in which the object is supposed to be
     * @param type the type of the object to turn on
     */
    static void turnObjectOn(Room room, String type){
        if (room.connectedObjects != null)
            for (ConnectedObject object : room.connectedObjects){
                if(object.type.equals(type)){
                    object.enable();
                    return;
                }
            }
        System.err.format("No %s in %s\n", type, room.name);
    }
}
