package main;

import java.io.BufferedWriter;

public class Helper {

    public static void sendCommand(BufferedWriter writer, String command){
        try{
            writer.write(command + "\n");
            writer.flush();
            writer.write("observe\n");
            writer.flush();
        }catch (Exception e){
            System.err.println("Error while sending command");
            e.printStackTrace();
        }
    }
}
