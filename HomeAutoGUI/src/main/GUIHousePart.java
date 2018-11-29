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
        pane.setAlignment(labelName, Pos.CENTER);
    }

    private void enter(){
        Helper.sendCommand(parent.writer, Command.walk + " " + name);
    }

    synchronized void setPerson(){
        // First wipe other rooms, then enter in this one
        parent.clearPersonsInRoom();
        Label person = new Label("Person");
        pane.setBottom(person);
        pane.setAlignment(person, Pos.CENTER);
    }

    synchronized void emptyPerson(){
        pane.setBottom(null);
    }

    synchronized void setLightIntensity(double value){
        pane.setStyle("-fx-background-color: rgba(255,250,0,"+ value +"/100);" +
                "-fx-border-color: black");
    }

    synchronized void triggerAlarm(){
        Label alarm = new Label(Character.toString((char)0x269E));
        pane.setRight(alarm);
        pane.setAlignment(alarm, Pos.CENTER);
    }
    synchronized void silenceAlarm(){
        pane.setRight(null);
    }

    synchronized  void openDoor(){
        Label openDoor = new Label("╗\n\n╝");
        pane.setLeft(openDoor);
        pane.setAlignment(openDoor, Pos.CENTER);
    }

    synchronized void closeDoor(){
        Label closedDoor = new Label("╗\n║\n╝");
        pane.setLeft(closedDoor);
        pane.setAlignment(closedDoor, Pos.CENTER);
    }
}
