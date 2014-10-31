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
    
    public String exec() throws IOException, InterruptedException {
        return executeCommand(getCommand());
    }
    
    public String getCommand() {
        return command;
    }
    
    private String executeCommand(String command) throws IOException, InterruptedException {
        notifyListenersOfStart(command);
        
        Process proc = Runtime.getRuntime().exec(command);
        
        ArrayList<String> arr = new ArrayList<>();
        
        BufferedReader procReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        BufferedReader errReader = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

        // read output of command
        String line = null, standardOutput = "",
               errorLine = null, errorOutput = "";
        while ((line = procReader.readLine()) != null || (errorLine = errReader.readLine()) != null) {
            if (line != null) {
                notifyListenersOfLine(line);
                standardOutput += line + "\n";
            }
            
            if (errorLine != null) {
                notifyListenersOfErrorLine(errorLine);
                errorOutput += errorLine + "\n";
            }
        }
        
        proc.waitFor();
        int exitValue = proc.exitValue();
        proc.destroy();
        
        notifyListenersOfFinished(standardOutput, errorOutput, exitValue);
        return standardOutput;
    }
    
    protected void notifyListenersOfStart(String command) {
        for (ProcListener listener : listeners) {
            listener.start(command);
        }
    }
    
    protected void notifyListenersOfLine(String line) {
        for (ProcListener listener : listeners) {
            listener.line(line);
        }
    }
    
    protected void notifyListenersOfErrorLine(String line) {
        for (ProcListener listener : listeners) {
            listener.errorLine(line);
        }
    }
    
    protected void notifyListenersOfFinished(String standardOutput, String errorOutput, int exitValue) {
        for (ProcListener listener : listeners) {
            listener.finished(standardOutput, errorOutput, exitValue);
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
