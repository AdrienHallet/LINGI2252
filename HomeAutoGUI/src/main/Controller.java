package main;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import system.HomeController;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Controller {

    private BlockingQueue<String> outputQueue = new ArrayBlockingQueue<>(100);

    @FXML
    private TextArea console;

    @FXML
    private TextField inputField;

    @FXML
    private MenuButton walkInButton;

    @FXML
    private TilePane tilePane;

    private PrintStream ps ;
    private String line;
    private InputStream in;

    BufferedWriter writer;
    BufferedReader reader;

    public void initialize() {
        ps = new PrintStream(new Console(console));
        try {
            Scanner scan = new Scanner(System.in);
            String[] commands = {"java", "-jar", "HomeController.jar", "config_big.json"};
            ProcessBuilder builder = new ProcessBuilder(commands);
            builder.redirectErrorStream(true);
            Process process = builder.start();

            OutputStream stdin = process.getOutputStream ();
            InputStream stderr = process.getErrorStream ();
            InputStream stdout = process.getInputStream ();

            reader = new BufferedReader (new InputStreamReader(stdout));
            writer = new BufferedWriter(new OutputStreamWriter(stdin));


            UpdateThread updateThread = new UpdateThread(new Scanner(reader), ps, outputQueue);
            updateThread.start();

            //Set focus on inputField at startup
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    buildLayout();
                    inputField.requestFocus();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public class Console extends OutputStream {
        private TextArea console;

        public Console(TextArea console) {
            this.console = console;
        }

        public void appendText(String valueOf) {
            Platform.runLater(() -> console.appendText(valueOf));
        }

        public void write(int b) throws IOException {
            appendText(String.valueOf((char)b));
        }
    }

    public void buildLayout(){
        try{
            writer.write(Command.layout);
            writer.flush();
            List<String> rooms = new ArrayList<>();
            while(true){

                String response = outputQueue.take();
                System.out.println(response);
                if (response.equals("")){break;}
                if (!response.contains("House layout:")){
                    rooms.add(response);
                }
            }
            for (String room : rooms){
                BorderPane newRoom = new BorderPane();
                newRoom.setMinSize(150, 150);
                newRoom.setStyle("-fx-background-color: #FFFFFF;");
                newRoom.setStyle("-fx-border-color: #000000;");
                newRoom.addEventHandler(MouseEvent.MOUSE_PRESSED,
                        event -> {walkIn(room);
                        ObservableList<Node> borderPanes = tilePane.getChildren();
                        for (Node bp : borderPanes)
                            ((BorderPane) bp).setBottom(null);
                        Label person = new Label("🚶");
                        person.setFont(new Font(16));
                        newRoom.setBottom(person);
                        newRoom.setAlignment(person, Pos.CENTER);
                        });
                Label name = new Label(room);
                newRoom.setTop(name);
                tilePane.getChildren().add(newRoom);
            }
            System.out.println("Added rooms: " + rooms.size());

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    void walkIn(String room){
        sendCommand(Command.walk + " " + room);
    }

    public void sendCommand(String command){
        try{
            writer.write(command + "\n");
            writer.flush();
        }catch (Exception e){
            System.err.println("Error while sending command");
            e.printStackTrace();
        }
    }

    @FXML
    public void onEnter(ActionEvent ae){
        sendCommand(inputField.getText());
        inputField.setText("");
    }
}
