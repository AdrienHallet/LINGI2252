package main;

import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * Singleton class handling the house layout visuals
 */
public class GUIHouse {

    private static GUIHouse instance = null;
    BufferedWriter writer;
    TilePane tilePane;

    private List<GUIHousePart> houseParts;

    private GUIHouse(BufferedWriter writer, TilePane tilePane){
        this.writer = writer;
        this.tilePane = tilePane;
        this.houseParts = new ArrayList<>();
        instance = this;
    }

    public static GUIHouse getOrCreate(BufferedWriter writer, TilePane tilePane){
        if(instance != null)
            return instance;

        return new GUIHouse(writer, tilePane);

    }

    public static GUIHouse getInstance(){
        return instance;
    }

    public void buildLayout(List<String> rooms){
        try{
//            writer.write(Command.layout);
//            writer.flush();
//            List<String> rooms = new ArrayList<>();
//            while(true){
//
//                String response = outputQueue.take();
//                System.out.println(response);
//                if (response.equals("")){break;}
//                if (!response.contains("House layout:")){
//                    rooms.add(response);
//                }
//            }
            for (String room : rooms){
                houseParts.add(new GUIHousePart(room));
            }
            houseParts.forEach(
                    guiHousePart -> tilePane.getChildren().add(guiHousePart.pane)
            );
            System.out.println("Added rooms: " + rooms.size());

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    GUIHousePart getRoomByName(String name){
        for (GUIHousePart part : houseParts){
            if (part.name.equals(name))
                return part;
        }
        return null;
    }

    public void clearPersonsInRoom(){
        houseParts.forEach(part -> part.emptyPerson());
    }
}
