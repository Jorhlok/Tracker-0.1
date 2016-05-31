package net.jorhlok.tracker0_1.playroutine;

import com.badlogic.gdx.utils.async.AsyncTask;
import java.util.Arrays;
import net.jorhlok.jpsg.JPSG;

/**
 * Used by an AudioInterface to call the PlayRoutine(s) in another thread so it can get back to playing audio.
 * @author jorh
 */
public class AsyncPlay implements AsyncTask {
    
    public PlayRoutine pr;
    public JPSG chip;
    public short[] output;
    public int buffer = 44100/60;
    @Override
    public Object call() {
        return use();
    }

    public Object use() {
        System.err.println("aplay.call();");
        if (chip == null || pr == null) return -1;
//        pr.update();
        System.err.println("pr.update(); //done");
        chip.setStepper( 0, (int)Math.round(440.0/44100*(1<<24) ) );//pr.Channels1[0].Stepper);
        System.err.println("setstep");
        chip.setSamples(0, pr.data.PCM4[15]);
        System.err.println("setsamp");
        chip.setnSamples(0, 2);
        System.err.println("setn");
        chip.setStereo(0, new int[] {3,3,3,3, 3,3,3,3});
        System.err.println("sets");
        chip.setVolume(0, 15);
        System.err.println("setv");
        chip.setWidth(0, 15);
        System.err.println("setw");
        
        output = new short[buffer*2];
        System.err.println("output = new short[buffer*2];");
        short[] samp;
        for (int i=0; i<output.length; i+=2) {
            chip.step();
            samp = chip.output();
            if (samp == null || samp.length != 2) return -2;
            output[i] = samp[0];
            output[i+1] = samp[1];
        }
        System.err.println(Arrays.toString(output));
        return 0;
    }
    
}
