package main;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

class GUIHousePart {

    String name;
    BorderPane pane;

    private GUIHouse parent;

    GUIHousePart(String name){
        this.name = name;
        this.parent = GUIHouse.getInstance();

        createPane();
    }

    private void createPane(){
        pane = new BorderPane();
        pane.setMinSize(150, 150);
        pane.setStyle("-fx-background-color: #FFFFFF;");
        pane.setStyle("-fx-border-color: #000000;");
        pane.addEventHandler(MouseEvent.MOUSE_PRESSED,
                event -> {enter();
                    // Todo movePerson(room);
                });
        Label labelName = new Label(this.name);
        pane.setLeft(new BorderPane());
        pane.setTop(labelName);
        BorderPane.setAlignment(labelName, Pos.CENTER);
    }

    private void enter(){
        Helper.sendCommand(parent.writer, Command.walk + " " + name);
    }

    synchronized void setPerson(){
        // First wipe other rooms, then enter in this one
        parent.clearPersonsInRoom();
        Label person = new Label("Person");
        pane.setBottom(person);
        BorderPane.setAlignment(person, Pos.CENTER);
    }

    synchronized void emptyPerson(){
        pane.setBottom(null);
    }

    synchronized void setLightIntensity(double value){
        pane.setStyle("-fx-background-color: rgba(255,250,0,"+ value +"/100);" +
                "-fx-border-color: black");
    }

    synchronized void triggerAlarm(){
        Label alarm = new Label("࿄");
        alarm.setFont(new Font(40));
        pane.setRight(alarm);
        BorderPane.setAlignment(alarm, Pos.CENTER);
    }
    synchronized void silenceAlarm(){
        pane.setRight(null);
    }

    synchronized  void openDoor(){
        Label openDoor = new Label("╗\n\n╝");
        ((BorderPane)pane.getLeft()).setTop(openDoor);
    }

    synchronized void closeDoor(){
        Label closeDoor = new Label("╗\n║\n╝");
        ((BorderPane) pane.getLeft()).setTop(closeDoor);
    }

    synchronized void heat(){
        Label heat = new Label("♨️");
        heat.setFont(new Font("Arial",42));
        pane.setCenter(heat);
        BorderPane.setAlignment(heat, Pos.CENTER);
    }

    synchronized void noHeat(){
        pane.setCenter(null);
    }

    synchronized void lock(){
        lockHelper("L");
    }

    synchronized void unlock(){
        lockHelper("U\n");
    }

    private void lockHelper(String u) {
        Label lock = new Label(u);
        ((BorderPane)pane.getLeft()).setCenter(lock);
    }
}
