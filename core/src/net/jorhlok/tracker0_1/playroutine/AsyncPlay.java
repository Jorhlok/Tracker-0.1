package net.jorhlok.tracker0_1.playroutine;

import com.badlogic.gdx.utils.async.AsyncTask;
import net.jorhlok.jpsg.JPSG;

/**
 * Used by an AudioInterface to call the PlayRoutine(s) in another thread so it can get back to playing audio.
 * @author jorh
 */
public class AsyncPlay implements AsyncTask {
    
    public PlayRoutine pr;
    public JPSG chip;
    public short[] output;

    @Override
    public Object call() {
        output = new short[800];
        for (int i=0; i<output.length; ++i) {
            if (i%100 < 50) output[i] = 10000;
            else output[i] = -10000;
        }
        return 0;
    }
    
}
