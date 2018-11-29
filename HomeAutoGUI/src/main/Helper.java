package main;

import java.io.BufferedWriter;

public class Helper {

    public static void sendCommand(BufferedWriter writer, String command){
        try{
            GUIHouse house = GUIHouse.getInstance();
            writer.write(command + "\n");
            writer.flush();
            for (GUIHousePart part : house.houseParts){
                writer.write("observe " + part.name + "\n");
                writer.flush();
            }
        }catch (Exception e){
            System.err.println("Error while sending command");
            e.printStackTrace();
        }
    }
}
