package main;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.io.*;
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
    private VBox buttonBox;

    @FXML
    private TilePane tilePane;

    private PrintStream ps ;
    private String line;
    private InputStream in;

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

        public void write(int b) throws IOException {
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

}
