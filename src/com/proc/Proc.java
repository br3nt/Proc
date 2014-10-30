package com.proc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author Brent Jacobs
 */
public class Proc {
    
    private String command;
    private ArrayList<ProcListener> listeners;
    
    public Proc(String command) {
        this.command = command;
        this.listeners = new ArrayList<>();
    }
    
    public void addListener(ProcListener listener) {
        listeners.add(listener);
    }
    
    public String exec() throws IOException {
        return executeCommand(getCommand());
    }
    
    public String getCommand() {
        return command;
    }
    
    private String executeCommand(String command) throws IOException {
        notifyListenersOfStart();
        
        Process proc = Runtime.getRuntime().exec(command);
        BufferedReader procReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));

        // read output of nmap command
        String line, output = "";
        while ((line = procReader.readLine()) != null) {
            notifyListenersOfLine(line);
            output += line + "\n";
        }
        
        notifyListenersOfFinished(output);
        return output;
    }
    
    protected void notifyListenersOfStart() {
        for (ProcListener listener : listeners) {
            listener.start();
        }
    }
    
    protected void notifyListenersOfLine(String line) {
        for (ProcListener listener : listeners) {
            listener.line(line);
        }
    }
    
    protected void notifyListenersOfFinished(String output) {
        for (ProcListener listener : listeners) {
            listener.finished(output);
        }
    }
    
//    public String sudoExec(String password) {
//        return executeCommand(getSudoCommand());
//    }
    
//    public String getSudoCommand(String password) {
//        // see: http://stackoverflow.com/questions/14015829/how-to-supply-sudo-with-root-password-from-java
//        // this will only work on linux
//        return "echo " + password  + " | sudo -S " + command;
//    }
    
    
    
}
