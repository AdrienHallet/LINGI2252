package system;

import controllers.sensors.Sensor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

public class Scenario {

    private HomeController controller;
    private House house;
    private HousePart currentHousePart;
    private Scanner userInput = new Scanner(System.in); // Get ready to receive inputs
    private BufferedReader reader;

    /**
     * Create a new user scenario
     * @param house the house configuration
     */
    public Scenario(House house, HomeController controller){
        this.house = house;
        this.controller = controller;
        currentHousePart = house.housePartList.get(0); // Start in the first configured housePart
    }

    /**
     * Create a new text scenario
     * @param house the house configuration
     * @param filePath the scenario
     */
    public Scenario(House house, HomeController controller, String filePath){
        this.house = house;
        this.controller = controller;
        currentHousePart = house.housePartList.get(0);
        try {
            reader = new BufferedReader(new FileReader(filePath));
        } catch(Exception e){
            System.err.println("Failed to read scenario file: " + e.toString());
        }
    }

    /**
     * Read a user command
     */
    void userInput(){
        System.out.println("> What will you do now?");
        String action = userInput.nextLine();
        doSomething(action);
        controller.controller_loop();
        System.out.flush();
    }

    /**
     * Read a command from file
     */
    void fileInput(){
        try {
            System.out.println("*Tick*");
            String action = reader.readLine();
            if (action != null)
                doSomething(action);
            else
                return;
        }catch (Exception e){
            System.err.println("Error while reading scenario: " + e.toString());
        }
        controller.controller_loop();
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
                System.exit(0);
            case "help":
                // Request for help
                printHelp();
                break;
            case "layout":
                System.out.println("House layout:");
                house.housePartList.forEach(housePart -> System.out.println(housePart.name));
                System.out.println();
                break;
            case "position":
                // Get current position
                System.out.println(currentHousePart.name);
                break;
            case "observe":
                currentHousePart.printState();
                break;
            case "walk":
                // Walk to next housePart
                walk();
                break;
            case "system.reset":
                // Reset all the sensors to 0
                for(HousePart cHousePart : house.housePartList){
                    if(cHousePart.sensors != null) {
                        for (Sensor sensor : cHousePart.sensors) {
                            sensor.reset(); // Resetting a sensor will cascade the reset to the linked actuators
                        }
                    }
                }
                break;
            default:
                if(action.toLowerCase().startsWith("housePart.temperature")){
                    double temperature = Double.parseDouble(action.replace("housePart.temperature ",""));
                    FakeEvent.setTemperature(house.getHousePartByName(currentHousePart.name), temperature);
                } else if (action.toLowerCase().startsWith("walk ")){
                    String housePart = action.replace("walk ", "");
                    walk(housePart);
                } else if (action.toLowerCase().startsWith("set ")) {
                    String[] params = action.replace("set ", "").split(" ");
                    currentHousePart.setActuatorTypeToValue(params[0], Double.parseDouble(params[1]));
                } else if (action.toLowerCase().startsWith("detect ")){
                    String[] params = action.replace("detect ", "").split(" ");
                    currentHousePart.setSensorTypeToValue(params[0], Double.parseDouble(params[1]));
                } else if (action.toLowerCase().startsWith(("toggle "))) {
                    String[] params = action.replace("toggle ", "").split(" ");
                    controller.toggleObject(params[0], params[1]);
                } else if (action.toLowerCase().startsWith(("enable "))) {
                    String[] params = action.replace("enable ", "").split(" ");
                    controller.enableActuator(params[0], params[1]);
                } else if (action.toLowerCase().startsWith(("disable "))) {
                    String[] params = action.replace("disable ", "").split(" ");
                    controller.disableActuator(params[0], params[1]);
                } else if (action.toLowerCase().startsWith("fire ")){
                    String housePart = action.replace("fire ", "");
                    FakeEvent.startFire(house.getHousePartByName(housePart));
                } else if (action.toLowerCase().startsWith("observe ")){
                    String housePart = action.replace("observe ", "");
                    house.getHousePartByName(housePart).printState();
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
    private void printHelp(){
        System.out.println("Home Automation System Group G :\n" +
                "walk (<house_part>) : walk to another house_part (or directly to <house_part>)\n" +
                "set <actuator> <value> : set an actuator in current house_part of type <actuator> to double <value>\n" +
                "detect <sensor> <value> : set a sensor in current house_part of type <sensor> to double <sensor>\n" +
                "toggle <connected_object> : toggle a connected object of type <connected_object> in the current room\n" +
                "observe (<house_part>) : observe the current house_part or the specified one\n" +
                "enable <house_part> <actuator> : enable <actuator> in <house_part>\n" +
                "disable <house_part> <actuator> : disable <actuator> in <house_part>\n" +
                "");
    }

    /**
     * Let user choose a housePart from available houseParts
     */
    private void walk() {
        System.out.println("From " + currentHousePart.name + " you can reach:");
        for (String housePart : currentHousePart.accessibleHouseParts) {
            System.out.println(housePart);
        }
        String newHousePart = userInput.nextLine();
        for (HousePart cHousePart : house.housePartList) {
            if (cHousePart.name.equalsIgnoreCase(newHousePart)) {
                currentHousePart = cHousePart;
                System.out.println("Walking into " + currentHousePart.name);
            }
        }
    }

    /**
     * Walk straight into
     * @param housePart the housePart
     */
    private void walk(String housePart){
        for (HousePart cHousePart : house.housePartList) {
            if (cHousePart.name.equalsIgnoreCase(housePart)) {
                FakeEvent.resetMotion(currentHousePart);
                currentHousePart = cHousePart;
                FakeEvent.detectMotion(currentHousePart);
                System.out.println("Walking into " + currentHousePart.name);
            }
        }
    }
}
