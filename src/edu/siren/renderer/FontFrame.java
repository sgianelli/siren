package edu.siren.renderer;

public class FontFrame {
    public String message, wav = null;
    public double msecTotal, msecFrame, msecWait;
    
    public FontFrame(String message, double msec, double msecWait) {
        this.message = message;
        this.msecTotal = msec;
        this.msecFrame = msec / (float) message.length();
        this.msecWait = msecWait;
    }
    
    public FontFrame(String message, double msec, double msecWait, 
                     String wav) 
    {
        this(message, msec, msecWait);
        this.wav = wav;        
    }
}
