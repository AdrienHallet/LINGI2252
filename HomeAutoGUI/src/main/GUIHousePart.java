package main;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;

public class GUIHousePart {

    String name;
    BorderPane pane;

    private GUIHouse parent;

    public GUIHousePart(String name){
        this.name = name;
        this.parent = GUIHouse.getInstance();

        createPane();
    }

    public void createPane(){
        pane = new BorderPane();
        pane.setMinSize(150, 150);
        pane.setStyle("-fx-background-color: #FFFFFF;");
        pane.setStyle("-fx-border-color: #000000;");
        pane.addEventHandler(MouseEvent.MOUSE_PRESSED,
                event -> {enter();
                    // Todo movePerson(room);
                });
        Label labelName = new Label(this.name);
        pane.setTop(labelName);
    }

    private void enter(){
        Helper.sendCommand(parent.writer, Command.walk + " " + name);
    }

    void setPerson(){
        // First wipe other rooms, then enter in this one
        parent.clearPersonsInRoom();
        Label person = new Label("Person");
        pane.setBottom(person);
        pane.setAlignment(person, Pos.CENTER);
    }

    void emptyPerson(){
        pane.setBottom(null);
    }
}
