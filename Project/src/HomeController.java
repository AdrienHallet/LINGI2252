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
        for (HousePart cHousePart : myHouse.housePartList){
            if(cHousePart.sensors != null) {
                for (Sensor cSensor : cHousePart.sensors) {
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
        for (HousePart cHousePart : myHouse.housePartList){
            if (cHousePart.actuators != null) {
                // Display the states of the actuators, per sensor, per housePart
                for (Sensor sensor : cHousePart.sensors) {
                    for (Actuator actuator : sensor.getActuatorList()) {
                        if (actuator.isTriggered()) {
                            System.out.printf("[%s:%s]: %s\n", cHousePart.name, actuator.type, actuator.getStateAsString());
                        }
                    }
                }
                // Display the states of the connected objects, per housePart
            }
            if (cHousePart.connectedObjects != null){
                for (ConnectedObject connectedObject : cHousePart.connectedObjects){
                    if (connectedObject.isTriggered()){
                        System.out.printf("[%s:%s]: %s\n", cHousePart.name, connectedObject.type, connectedObject.getStateAsString());
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
            if (sensor.shouldBroadcast()) { // Trigger all
                for (HousePart cHousePart : myHouse.housePartList)
                    for (Actuator cSubActuator : cHousePart.actuators)
                        if (cSubActuator.type.equals(cActuator.type))
                            cSubActuator.trigger();
            }
            else { // Trigger one in given housePart
                HousePart housePartToActivate = myHouse.getHousePartByName(cActuator.linkHousePartName);
                for (Actuator rActuator : housePartToActivate.actuators){
                    if (rActuator.type.equalsIgnoreCase(cActuator.type))
                        rActuator.trigger();
                }
            }
        }
    }

    /**
     * Turn a connected object on
     * @param housePart the housePart in which the object is supposed to be
     * @param type the type of the object to turn on
     */
    static void toggleObject(HousePart housePart, String type){
        if (housePart.connectedObjects != null)
            for (ConnectedObject object : housePart.connectedObjects){
                if(object.type.equals(type)){
                    object.toggle();
                    return;
                }
            }
        System.err.format("No %s in %s\n", type, housePart.name);
    }
}
