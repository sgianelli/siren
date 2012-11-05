package edu.siren.renderer;

public class FontFrame {
    public String message;
    public double msecTotal, msecFrame, msecWait;
    
    public FontFrame(String message, double msec, double msecWait) {
        this.message = message;
        this.msecTotal = msec;
        this.msecFrame = msec / (double)message.length();
        this.msecWait = msecWait;
        System.out.println("" + this.msecFrame + " per frame");
    }
}
