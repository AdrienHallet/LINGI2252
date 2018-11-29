package main;

import javafx.application.Platform;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateThread extends Thread {

    Scanner scanner;
    PrintStream ps;
    GUIHouse guiHouse;

    public UpdateThread(Scanner scanner, PrintStream ps, GUIHouse guiHouse){
        this.scanner = scanner;
        this.ps = ps;
        this.guiHouse = guiHouse;
    }

    public void run(){
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            if (!line.equalsIgnoreCase("> What will you do now?")) {
                interpret(scanner, line);
            }
            ps.println(line);
        }
    }

    private void interpret(Scanner scanner, String response){
        if(response.equalsIgnoreCase("house layout:")){
            boolean flag = true;
            ArrayList<String> rooms = new ArrayList<>();
            while(flag){
                String line = scanner.nextLine();
                if(line.equals(""))
                    flag = false;
                else
                    rooms.add(line);
            }
            requestBuildLayout(rooms);
        }
        if(response.startsWith("Walking into")){
            requestWalk(response.replace("Walking into ", ""));
        }
        if(response.contains("light")){
            requestLightIntensity(response);
        }
    }

    private String[] extractActuator(String state){
        Matcher m = Pattern.compile("\\[([^)]+)\\]").matcher(state);
        if (m.find()) {
            String actuator = m.group(0);
            actuator = actuator.replace("[", "").replace("]","");
            return actuator.split(":");
        }
        return null;
    }

    private void requestWalk(String room){
        Platform.runLater(() -> guiHouse.getRoomByName(room).setPerson());
    }

    private void requestBuildLayout(ArrayList<String> rooms){
        Platform.runLater(() -> guiHouse.buildLayout(rooms));
    }

    private void requestLightIntensity(String response){
        String[] actuator = extractActuator(response);
        Matcher m = Pattern.compile("\\d+(\\%)").matcher(response);
        double value = 0;
        if (m.find()){
            value = Double.parseDouble(m.group(0).replace("%",""));
        }
        double finalValue = value;
        Platform.runLater(() -> guiHouse.getRoomByName(actuator[0]).setLightIntensity(finalValue));
    }
}
