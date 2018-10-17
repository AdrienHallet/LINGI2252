import controllers.actuators.Actuator;
import controllers.sensors.Sensor;

import java.util.Scanner;

public class Automation {

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
            case "help":
                System.out.println("Nope.");
                break;
            case "position":
                System.out.println(currentRoom);
                break;
            case "walk":
                System.out.println("From " + currentRoom.name + " you can reach:");
                for (String room : currentRoom.accessibleRooms){
                    System.out.println(room);
                }
                String newRoom = userInput.nextLine();
                for (Room cRoom : myHouse.roomList) {
                    if (cRoom.name.equalsIgnoreCase(newRoom)) {
                        currentRoom = cRoom;
                        System.out.println("Waling into " + currentRoom.name);
                    }
                }
                break;
            case "fire":
                currentRoom.startFire();
                break;
            case "exit":
                return;
            default:
                System.out.println("I did not get that ...");
        }
        controller_loop();
    }

    static void controller_loop(){
        /* Send the events to the controllers around the house */
        for (Room cRoom : myHouse.roomList){
            if(cRoom.sensors != null) {
                for (Sensor cSensor : cRoom.sensors) {
                    if (cSensor.isTriggered()) {
                        switch (cSensor.type) {
                            case "motion":
                                detectMotion(cRoom);
                                break;
                            case "smoke-detector":
                                triggerAudioAlarm(cRoom);
                                break;
                        }
                    }
                }
            }
        }
        state_loop();
    }

    static void state_loop(){
        /* Display the events happening to the user via terminal */
        for (Room cRoom : myHouse.roomList){
            if (cRoom.actuators != null) {
                for (Actuator cActuator : cRoom.actuators) {
                    if (cActuator.isTriggered()) {
                        System.out.printf("[%s]: %s\n", cRoom.name, cActuator.getStateAsString());
                    }
                }
            }
        }
        action_loop();
    }

    static void triggerAudioAlarm(Room room){
        for (Actuator cActuator : room.actuators){
            if (cActuator.type.equals("audio-alarm")) {
                cActuator.update(100.0);
            }

        }
    }

    static void detectMotion(Room room){
        //ToDo
    }
}
