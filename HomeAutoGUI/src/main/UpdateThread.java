package main;

import java.io.PrintStream;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

public class UpdateThread extends Thread {

    Scanner scanner;
    PrintStream ps;
    BlockingQueue<String> pending;
    GUIHouse guiHouse;

    public UpdateThread(Scanner scanner, PrintStream ps, BlockingQueue<String> queue){
        this.scanner = scanner;
        this.ps = ps;
        this.pending = queue;
    }

    public void run(){
        while(scanner.hasNextLine()){
            try {
                String line = scanner.nextLine();
                if (line.equalsIgnoreCase("> What will you do now?"))
                    pending.put(line);
                ps.println(line);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
