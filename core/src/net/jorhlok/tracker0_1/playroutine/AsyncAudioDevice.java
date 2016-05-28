package net.jorhlok.tracker0_1.playroutine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.badlogic.gdx.utils.async.AsyncResult;
import com.badlogic.gdx.utils.async.AsyncTask;

/**
 * Runs a thread to convert libGDX's blocking audio method into a non-blocking one.
 * @author jorh
 */
public class AsyncAudioDevice implements AsyncTask, AudioInterface{
    
    public AudioDevice device;
    short[] curBuf;
    public short[] dispBuf;
    
    public boolean disp = false;
    public boolean quit = false;
    public boolean hasquit = false;
    public boolean underflow = false;
    public int samplerate = 44100;
    
    public AsyncExecutor exe;
    public AsyncPlay aplay;
    AsyncResult ar;
    
    @Override
    public int create() {
        exe = new AsyncExecutor(2);
        if (exe == null) return 1;
        device = Gdx.audio.newAudioDevice(samplerate, false);
        if (device == null) {
            exe.dispose();
            return 2;
        }
        exe.submit(this);
        return 0;
    }
    
    @Override
    public void dispose() {
        quit = true;
        /*/while(!hasquit) {
            //wait until we have indeed quit
        }//*/
        if (exe != null) exe.dispose();
        if (device != null) device.dispose();
    }

    @Override
    public Object call() {
        if (quit || exe == null || aplay == null) {
            hasquit = true;
            return 0;
        }
        hasquit = false;
        underflow = false;
        boolean uf = false;
        
        //prime the pump
        aplay.call(); //calling this directly doesn't spawn a new thread
        while (!quit) {
            while (ar != null && !ar.isDone()) { //wow dat slow
                uf = true;
            }
            if (uf) {
                uf = false;
                underflow = true;
                System.err.println("underflow");
            }
            curBuf = aplay.output;
            if (disp) dispBuf = curBuf.clone(); //for scope visualization
            //launch thread to generate next buffer
            if (!quit) ar = exe.submit(aplay);
            //dump current buffer into audio device
            if (!quit) device.writeSamples(curBuf, 0, curBuf.length);
        }
        //dispose of stuff
        hasquit = true;
        return 0;
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
        return dispBuf;
    }
    
}
