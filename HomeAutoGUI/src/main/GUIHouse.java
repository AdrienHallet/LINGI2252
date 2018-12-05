package main;

import javafx.scene.layout.TilePane;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class handling the house layout visuals
 */
class GUIHouse {

    private static GUIHouse instance = null;
    BufferedWriter writer;
    private TilePane tilePane;

    List<GUIHousePart> houseParts;

    private GUIHouse(BufferedWriter writer, TilePane tilePane){
        this.writer = writer;
        this.tilePane = tilePane;
        this.houseParts = new ArrayList<>();
        instance = this;
    }

    static GUIHouse getOrCreate(BufferedWriter writer, TilePane tilePane){
        if(instance != null)
            return instance;

        return new GUIHouse(writer, tilePane);

    }

    static GUIHouse getInstance(){
        return instance;
    }

    void buildLayout(List<String> rooms){
        try{
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

    void clearPersonsInRoom(){
        houseParts.forEach(GUIHousePart::emptyPerson);
    }
}
