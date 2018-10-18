import controllers.sensors.Sensor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

public class Scenario {

    private House house;
    private Room currentRoom;
    private Scanner userInput = new Scanner(System.in); // Get ready to receive inputs
    private BufferedReader reader;

    //DEBUG
    private final boolean debug = false; // 1 second delay between actions in text scenario to read user output


    /**
     * Create a new user scenario
     * @param house the house configuration
     */
    public Scenario(House house){
        this.house = house;
        currentRoom = house.roomList.get(0); // Start in the first configured room
    }

    /**
     * Create a new text scenario
     * @param house the house configuration
     * @param filePath the scenario
     */
    public Scenario(House house, String filePath){
        this.house = house;
        currentRoom = house.roomList.get(0);
        try {
            reader = new BufferedReader(new FileReader(filePath));
        } catch(Exception e){
            System.err.println("Failed to read scenario file: " + e.toString());
        }
    }

    /**
     * Read a user command
     */
    public void userInput(){
        System.out.println("> What will you do now?");
        String action = userInput.nextLine();
        doSomething(action);
        HomeController.controller_loop();
    }

    /**
     * Read a command from file
     */
    public void fileInput(){
        try {
            if(debug)
                Thread.sleep(1000);
            System.out.println("*Tick*");
            String action = reader.readLine();
            if (action != null)
                doSomething(action);
            else
                return;
        }catch (Exception e){
            System.err.println("Error while reading scenario: " + e.toString());
        }
        HomeController.controller_loop();
    }

    /**
     * Convert an action to the relevant triggers
     * @param action the action
     */
    private void doSomething(String action){
        switch (action.toLowerCase()) {
            /* Commands */
            case "": // Pass the current tick
                break;
            case "exit":
                // Exit the program
                return;
            case "help":
                // Request for help
                printHelp();
                break;
            case "position":
                // Get current position
                System.out.println(currentRoom.name);
                break;
            case "walk":
                // Walk to next room
                walk();
                break;
            case "room.fire":
                // Start a fire in the current room
                FakeEvent.startFire(currentRoom);
                break;
            case "room.dark":
                // Set the current room to darkness
                FakeEvent.setLight(currentRoom, 100.0); //ToDo change the light sensor to work on light amount (no inversion)
                break;
            case "room.music":
                // Turn the radio on in the room
                HomeController.turnObjectOn(currentRoom, "radio");
                break;
            case "system.reset":
                // Reset all the sensors to 0
                for(Room cRoom : house.roomList){
                    if(cRoom.sensors != null) {
                        for (Sensor sensor : cRoom.sensors) {
                            sensor.reset(); // Resetting a sensor will cascade the reset to the linked actuators
                        }
                    }
                }
                break;
            default:
                if(action.toLowerCase().startsWith("room.temperature")){
                    double temperature = Double.parseDouble(action.replace("room.temperature ",""));
                    FakeEvent.setTemperature(currentRoom, temperature);
                } else if (action.toLowerCase().startsWith("walk ")){
                    String room = action.replace("walk ", "");
                    walk(room);
                }

                else {
                    System.out.println("I did not get that ...");
                }
                break;
        }
    }

    /**
     * Print help
     */
    static void printHelp(){
        System.out.println("rtfm");
    }

    /**
     * Let user choose a room from available rooms
     */
    void walk() {
        System.out.println("From " + currentRoom.name + " you can reach:");
        for (String room : currentRoom.accessibleRooms) {
            System.out.println(room);
        }
        String newRoom = userInput.nextLine();
        for (Room cRoom : house.roomList) {
            if (cRoom.name.equalsIgnoreCase(newRoom)) {
                currentRoom = cRoom;
                System.out.println("Waling into " + currentRoom.name);
            }
        }
    }

    /**
     * Walk straight into
     * @param room the room
     */
    void walk(String room){
        for (Room cRoom : house.roomList) {
            if (cRoom.name.equalsIgnoreCase(room)) {
                currentRoom = cRoom;
                System.out.println("Waling into " + currentRoom.name);
            }
        }
    }
}
