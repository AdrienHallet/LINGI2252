package system;

import controllers.Link;
import controllers.actuators.Actuator;
import controllers.connectedObjects.ConnectedObject;
import controllers.sensors.Sensor;
import system.parametrization.BadConfigException;

public class HomeController {

    static House myHouse;
    static Scenario scenario;
    static boolean userInputScenario = true;

    /**
     * Launch the system
     * @param args the optional file to read the scenario from
     */
    public static void main (String[] args){
        try {
            myHouse = House.getOrCreate("src/config_big.json"); // Initialize the house from configuration
        } catch (BadConfigException e) {
            System.err.println("The house configuration doesn't implement all constraints: "+e.getMessage());
        }

        if (args.length == 0) {
            scenario = new Scenario(myHouse);
            try {
                scenario.userInput();
            } catch (BadConfigException e) {
                System.err.println("A constraint was violated: "+e.getMessage());
            }
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
                    else {
                        resetActions(cSensor);
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
            // Display the states of the actuators, per sensor, per housePart
            for (Actuator actuator : cHousePart.actuators){
                if (actuator.isTriggered()){
                    System.out.printf("[%s:%s]: %s\n", cHousePart.name, actuator.type, actuator.getStateAsString());
                }
            }
            // Display the states of the connected objects, per housePart
            for (ConnectedObject connectedObject : cHousePart.connectedObjects) {
                if (connectedObject.isTriggered()) {
                    System.out.printf("[%s:%s]: %s\n", cHousePart.name, connectedObject.type, connectedObject.getStateAsString());
                }
            }
        }
        if (userInputScenario) {
            try {
                scenario.userInput();
            } catch (BadConfigException e) {
                System.err.println("A constraint was violated: "+e.getMessage());
            }
        }
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
                if (!sensor.isInverted())
                    cActuator.trigger();
                else
                    cActuator.reset();
            }
        }
    }
    static void resetActions(Sensor sensor){
        Actuator[] aList = sensor.getActuatorList();
        for(Actuator cActuator : aList){
            if (sensor.shouldBroadcast()) { // Trigger all
                for (HousePart cHousePart : myHouse.housePartList)
                    for (Actuator cSubActuator : cHousePart.actuators)
                        if (cSubActuator.type.equals(cActuator.type))
                            cSubActuator.reset();
            }
            else { // Trigger one in given housePart
                if (!sensor.isInverted())
                    cActuator.reset();
                else
                    cActuator.trigger();
            }
        }
    }

    /**
     * Turn a connected object on
     * @param housePart the housePart in which the object is supposed to be
     * @param type the type of the object to turn on
     */
    static void toggleObject(String housePart, String type){
        HousePart location = myHouse.getHousePartByName(housePart);
        if (location == null){
            System.err.println("Unknown house part: " + housePart);
            return;
        }

        for (ConnectedObject object : location.connectedObjects){
            if(object.type.equals(type)){
                object.toggle();
                return;
            }
        }
        System.err.format("No %s in %s\n", type, housePart);
    }

    /**
     * Turn an actuator on
     * @param housePart the housePart in which the actuator is supposed to be
     * @param type the type of the actuator to turn on
     */
    static void enableActuator(String housePart, String type){
        HousePart location = myHouse.getHousePartByName(housePart);
        if (location == null){
            System.err.println("Unknown house part: " + housePart);
            return;
        }

        for (Actuator cActuator : location.actuators){
            if(cActuator.type.equals(type)){
                cActuator.enable();
                return;
            }
        }
        System.err.format("No %s in %s\n", type, housePart);
    }

    /**
     * Turn an actuator off
     * @param housePart the housePart in which the actuator is supposed to be
     * @param type the type of the actuator to turn off
     */
    static void disableActuator(String housePart, String type){
        HousePart location = myHouse.getHousePartByName(housePart);
        if (location == null){
            System.err.println("Unknown house part: " + housePart);
            return;
        }

        for (Actuator cActuator : location.actuators){
            if(cActuator.type.equals(type)){
                cActuator.disable();
                return;
            }
        }
        System.err.format("No %s in %s\n", type, housePart);
    }
}
