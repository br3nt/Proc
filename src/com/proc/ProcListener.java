package com.proc;

/**
 * The default implementation of ProcListener.
 * 
 * Simply stores the output of the executing Proc.
 * 
 * @author Brent Jacobs
 */
public class ProcListener {

    protected String output;
    
    public ProcListener() {
        this.output = "";
    }

    /**
     * Proc is about to start/restart.
     * 
     * Resets any captured output from the previous run.
     */
    public void start() {
        this.output = "";
    }

    /**
     * Proc is executing and ha captured a line of the output.
     * 
     * @param line  The next line of input
     */
    public void line(String line) {
        this.output += line + "\n";
    }
    
    /**
     * The Proc has finished executing.
     * 
     * The complete output of the executed proc is also provided.
     * 
     * @param output  The complete output
     */
    public void finished(String output) {}
}
