import controllers.actuators.Actuator;
import controllers.sensors.Sensor;

import java.util.Scanner;

public class HomeController {

    static House myHouse;
    static Scanner userInput;
    static Room currentRoom;

    public static void main (String[] args){
        myHouse = new House(); // Initialize the house from configuration
        userInput = new Scanner(System.in); // Get ready to receive inputs
        currentRoom = myHouse.roomList.get(0); // Start in the first configured room
        action_loop();
    }

    static void action_loop(){
        System.out.println("> What will you do now?");
        String action = userInput.nextLine();
        switch (action.toLowerCase()) {
            /* Commands */
            case "exit":
                // Exit the program
                return;
            case "help":
                // Request for help
                printHelp();
                break;
            case "position":
                // Get current position
                System.out.println(currentRoom);
                break;
            case "walk":
                // Walk to next room
                walk();
                break;
            case "room.fire":
                // Start a fire in the current room
                FakeEvent.startFire(currentRoom);
                break;
            case "room.cold":
                // Set the current room as cold
                FakeEvent.setTemperature(currentRoom, 15.0);
                break;
            case "system.reset":
                // Reset all the sensors to 0
                for(Room room : myHouse.roomList){
                    if(room.sensors != null) {
                        for (Sensor sensor : room.sensors) {
                            sensor.reset(); // Resetting a sensor will cascade the reset to the linked actuators
                        }
                    }
                }
                break;
            default:
                if(action.toLowerCase().startsWith("room.temperature")){
                    double temperature = Double.parseDouble(action.replace("room.temperature ",""));
                    FakeEvent.setTemperature(currentRoom, temperature);
                }else {
                    System.out.println("I did not get that ...");
                }
                break;
        }
        controller_loop();
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

    /* Display the events happening to the user via terminal */
    static void state_loop(){
        for (Room cRoom : myHouse.roomList){
            if (cRoom.actuators != null) {
                for (Sensor sensor : cRoom.sensors) {
                    for (Actuator actuator : sensor.getActuatorList()) {
                        if (actuator.isTriggered()) {
                            System.out.printf("[%s:%s]: %s\n", cRoom.name, actuator.type, actuator.getStateAsString());
                        }
                    }
                }
            }
        }
        action_loop();
    }

    /* Trigger all actions linked to a sensor */
    static void triggerActions(Sensor sensor){

        Actuator[] aList = sensor.getActuatorList();
        for(Actuator cActuator : aList){
            cActuator.trigger();
        }
    }

    /* Allow the user to walk from a room to another */
    static void walk() {
        System.out.println("From " + currentRoom.name + " you can reach:");
        for (String room : currentRoom.accessibleRooms) {
            System.out.println(room);
        }
        String newRoom = userInput.nextLine();
        for (Room cRoom : myHouse.roomList) {
            if (cRoom.name.equalsIgnoreCase(newRoom)) {
                currentRoom = cRoom;
                System.out.println("Waling into " + currentRoom.name);
            }
        }
    }

    /* Display commands to user */
    static void printHelp(){
        System.out.println("ToDo"); //todo
    }
}
