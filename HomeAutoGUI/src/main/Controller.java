package main;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Controller {

    @FXML
    private TextArea console;

    @FXML
    private TextField inputField;

    @FXML
    private TilePane tilePane;

    private PrintStream ps ;

    BufferedWriter writer;
    BufferedReader reader;

    private GUIHouse guiHouse;

    public void initialize() {
        ps = new PrintStream(new Console(console));
        try {
            String[] commands = {"java", "-jar", "HomeController.jar", "config_big.json"};
            ProcessBuilder builder = new ProcessBuilder(commands);
            builder.redirectErrorStream(true);
            Process process = builder.start();

            OutputStream stdin = process.getOutputStream ();
            InputStream stdout = process.getInputStream ();

            reader = new BufferedReader (new InputStreamReader(stdout));
            writer = new BufferedWriter(new OutputStreamWriter(stdin));

            guiHouse = GUIHouse.getOrCreate(writer, tilePane);

            UpdateThread updateThread = new UpdateThread(new Scanner(reader), ps, guiHouse);
            updateThread.start();

            writer.write(Command.layout);
            writer.flush();

            //Set focus on inputField at startup
            Platform.runLater(() -> inputField.requestFocus());

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

        public void write(int b) {
            if(console.getLength() > 5000){
                console.setText("");
            }
            appendText(String.valueOf((char)b));
        }
    }

    @FXML
    public void onEnter(){
        Helper.sendCommand(writer, inputField.getText());
        inputField.setText("");
    }

    @FXML
    public void humidity(){
        Helper.sendCommand(writer, "detect humidity 100");
    }

    @FXML
    public void nohumidity(){
        Helper.sendCommand(writer, "detect humidity 0");
    }

    @FXML
    public void cold(){
        Helper.sendCommand(writer, "detect thermometer 10");
    }

    @FXML
    public void hot(){
        Helper.sendCommand(writer, "detect thermometer 26");
    }

    @FXML
    public void bright(){
        Helper.sendCommand(writer, "detect light 100");
    }

    @FXML
    public void dark(){
        Helper.sendCommand(writer, "detect light 0");
    }

    @FXML
    public void smoke(){
        Helper.sendCommand(writer, "detect smoke 100");
    }

    @FXML
    public void nosmoke(){
        Helper.sendCommand(writer, "detect smoke 0");
    }

}
