package main;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class Controller {

    @FXML
    private TextArea console;
    private PrintStream ps ;

    public void initialize() {
        ps = new PrintStream(new Console(console)) ;
    }

    public void button(ActionEvent event) throws IOException {
        System.setOut(ps);
        System.setErr(ps);
        String command = "ls";
        Process child = Runtime.getRuntime().exec(command);

        InputStream in = child.getInputStream();
        int c;
        while ((c = in.read()) != -1) {
            System.out.print((char) c);
        }
        in.close();
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
}
