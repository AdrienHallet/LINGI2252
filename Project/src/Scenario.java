import controllers.sensors.Sensor;
import java.util.Scanner;

public class Scenario {

    House house;
    Room currentRoom;
    Scanner userInput = new Scanner(System.in); // Get ready to receive inputs
    String scenarioType;


    public Scenario(House house){
        this.house = house;
        currentRoom = house.roomList.get(0); // Start in the first configured room
    }

    public void userInput(){
        this.scenarioType = "user";
        System.out.println("> What will you do now?");
        String action = userInput.nextLine();
        switch (action.toLowerCase()) {
            /* Commands */
            case "exit":
                // Exit the program
                return;
            case "help":
                // Request for help
                HomeController.printHelp();
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
                FakeEvent.setLight(currentRoom, 100.0); //ToDo change the light sensor to work on light amount (no inversion)
                break;
            case "room.music":
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
                }else {
                    System.out.println("I did not get that ...");
                }
                break;
        }
        HomeController.controller_loop();
    }

    /* Allow the user to walk from a room to another */
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
}
