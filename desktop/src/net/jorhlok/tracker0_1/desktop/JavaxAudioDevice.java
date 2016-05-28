package net.jorhlok.tracker0_1.desktop;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.SourceDataLine;
import net.jorhlok.tracker0_1.playroutine.AsyncPlay;
import net.jorhlok.tracker0_1.playroutine.AudioInterface;

/**
 * Desktop implementation because
 * this ain't a perfect world
 * @author jorh
 */
public class JavaxAudioDevice implements AudioInterface, LineListener {

    AsyncPlay aplay;
    short[] curBuf;
    byte[] byteBuf = null;

    boolean quit = false;
    boolean hasquit = false;
    boolean underflow = false;

    SourceDataLine Output;
    int leader = 1024;
    boolean notDoneYet = false;
    boolean alreadyLead = false;

    @Override
    public int create() {
        quit = false;
        AudioFormat af = new AudioFormat(44100, 16, 2, true, false);
//        AudioFormat af = new AudioFormat(44100, 8, 1, true, false);
        try {
            Output = AudioSystem.getSourceDataLine(af);
        } catch (Exception e) {
            System.err.println("Failed to create audio device. " + e.getLocalizedMessage());
            return 1;
        }

        if (Output != null) {
            Output.addLineListener(this);
            try {
                Output.open(af,leader);
            } catch (Exception e) {
                System.err.println("Failed to open audio device. " + e.getLocalizedMessage());
                return 2;
            }
        }
        System.err.println("Created JavaxAudioDevice");
//        byte[] bytes = new byte[leader];
//        alreadyLead = true;
//        Output.write(bytes, 0, bytes.length);
//        Output.start();
        update(null);
        System.err.println(Output.getFormat().toString());
        return 0;
    }

    @Override
    public void dispose() {
        quit = true;
        Output.close();
        Output.flush();
        Output = null;
        hasquit = true;
    }

    @Override
    public void setAPlay(AsyncPlay p) {
        aplay = p;
    }

    @Override
    public AsyncPlay getAPlay() {
        return aplay;
    }

    @Override
    public boolean hasQuit() {
        return hasquit;
    }

    @Override
    public boolean hasUnderflow() {
        return underflow;
    }

    @Override
    public short[] getBuffer() {
        return curBuf.clone();
    }

//LineListener
    @Override
    public void update(LineEvent le) {
            if (le == null) System.err.println("Audio update");
            else System.err.println("Audio update " + le.toString());
            
        if ( !quit && ( le == null || le.getType() == LineEvent.Type.STOP ) ) {
            boolean uf = false;
            while (notDoneYet) {
                uf = true;
            }
            //race condition if you have more than one of these waiting on another
            //      if that happens, god help you
            notDoneYet = true;
            if (uf) {
                underflow = true;
                System.err.println("underflow");
            }
            
            if (byteBuf == null) { //insert some leader tape
                byteBuf = new byte[leader];
                for (int i = 0; i < byteBuf.length; ++i)
                    byteBuf[i] = 0;
                
                if (alreadyLead) System.err.println("Using audio leader when not supposed to!");
                else alreadyLead = true;
            }
            Output.write(byteBuf, 0, byteBuf.length);
            Output.start();
            byteBuf = null;
            if (!Output.isRunning()) { //debug
                System.err.println("Output not running");
            } else {
                System.err.println("Output running");
            }

            aplay.call();
            curBuf = aplay.output;
            if (curBuf == null) {
                //I don't know how to help you
                notDoneYet = false;
                System.err.println("Unable to fill buffer");
                return;
            }
            byteBuf = new byte[curBuf.length * 2];
            for (int i = 0; i < byteBuf.length; ++i) {
                //little endian
                if (i % 2 == 0) byteBuf[i] = (byte) (curBuf[i / 2]); //low byte
                else byteBuf[i] = (byte) (curBuf[i / 2] >> 8); //high byte
            }
            
//            byteBuf = new byte[curBuf.length/2]; //debug 8 bit mono
//            for (int i = 0; i < byteBuf.length; ++i) {
//                byteBuf[i] = (byte) (curBuf[i*2] >> 8);
//            }
            
            notDoneYet = false;
        }

    }
}
//*/