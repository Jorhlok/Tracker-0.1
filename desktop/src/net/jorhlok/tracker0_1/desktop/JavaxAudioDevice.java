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

    boolean quit = false;
    boolean hasquit = false;
    boolean underflow = false;

    SourceDataLine Output;
    int buffer = 44100/15;

    @Override
    public int create() {
        quit = false;
        AudioFormat af = new AudioFormat(44100, 16, 2, true, false);
        try {
            Output = AudioSystem.getSourceDataLine(af);
        } catch (Exception e) {
            System.err.println("Failed to create audio device. " + e.getLocalizedMessage());
            return 1;
        }

        if (Output != null) {
            Output.addLineListener(this);
            try {
                Output.open(af,buffer);
            } catch (Exception e) {
                System.err.println("Failed to open audio device. " + e.getLocalizedMessage());
                return 2;
            }
        }
        else {
            return 3;
        }
        
        Output.write(new byte[4], 0, 4);
        Output.start();
        return 0;
    }

    @Override
    public void dispose() {
        quit = true;
        Output.close();
        Output.flush();
        Output = null;
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
        System.err.println("Audio update " + le.toString());

        if (le.getType() == LineEvent.Type.STOP) {
            quit = true;
        }
        else if (le.getType() == LineEvent.Type.START) {
            byte[] byteBuf;
            quit = false;
            while (!quit) {
                aplay.use();
                curBuf = aplay.output;
                if (curBuf == null) {
                    underflow = true;
                    quit = true;
                }
                byteBuf = new byte[curBuf.length*2];
                for (int i=0; i<curBuf.length; i+=1) {
                    if ( (i&1) == 0) {
                        byteBuf[i*2] = (byte)curBuf[i];
                        byteBuf[i*2+1] = (byte)(curBuf[i]>>8);
                    }
                    else {
                        byteBuf[i*2] = (byte)curBuf[i];
                        byteBuf[i*2+1] = (byte)(curBuf[i]>>8);
                    }
                }
                Output.write(byteBuf, 0, byteBuf.length);
            }
            hasquit = true;
        }
//        else if (le.getType() == LineEvent.Type.OPEN) {
//            Output.start();
//        }

    }
}