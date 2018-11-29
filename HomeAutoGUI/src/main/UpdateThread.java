package main;

import javafx.application.Platform;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

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
                if(line.equals("")){
                    flag = false;
                }
                else {
                    rooms.add(line);
                }
            }
            requestBuildLayout(rooms);
        }
        if(response.startsWith("Walking into")){
            requestWalk(response.replace("Walking into ", ""));
        }
    }

    private void requestWalk(String room){
        Platform.runLater(() -> guiHouse.getRoomByName(room).setPerson());
    }

    private void requestBuildLayout(ArrayList<String> rooms){
        Platform.runLater(() ->guiHouse.buildLayout(rooms));
    }

}
