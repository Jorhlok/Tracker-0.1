package net.jorhlok.tracker0_1.playroutine;

import com.badlogic.gdx.utils.async.AsyncTask;
import net.jorhlok.jpsg.ChannelType1;
import net.jorhlok.jpsg.JPSG;

/**
 * Used by an AudioInterface to call the PlayRoutine(s) in another thread so it can get back to playing audio.
 * @author jorh
 */
public class AsyncPlay implements AsyncTask {
    
    public PlayRoutine pr;
    public JPSG chip = new JPSG(JPSG.Models.JPSG1608S);
    public short[] output;
    public int buffer = 44100/60;
    public ChannelType1 ch = new ChannelType1();
    
    @Override
    public Object call() {
        return use();
    }

    public Object use() {
//        System.err.println("aplay.call();");
        if (chip == null || pr == null) return -1;
        pr.update();

        int[] stereo = new int[pr.Channels1.length];
        int[] noise = new int[pr.Channels1.length];
        for (int i=0; i<pr.Channels1.length; ++i) {
            chip.setStepper(i, pr.Channels1[i].getStepper());
            if (pr.Channels1[i].Retrig) {
                pr.Channels1[i].Retrig = false;
                chip.setCounter(i, 0);
            }
            chip.setSamples(i, pr.data.PCM4[pr.Channels1[i].getSamples()]);
            chip.setnSamples(i, pr.Channels1[i].getNSamples());
            chip.setWidth(i, pr.Channels1[i].getWidth());
            stereo[i] = pr.Channels1[i].getStereo();
            noise[i] = pr.Channels1[i].getNoise()?1:0;
        }
//        System.err.println("updated chip");
        
        chip.setStereo(1, stereo);
        chip.setNoise(noise);
        
        output = new short[buffer*2];
        short[] samp;
        for (int i=0; i<output.length; i+=2) {
            chip.step();
            samp = chip.output();
            if (samp == null || samp.length != 2) return -2;
            output[i] = samp[0];
            output[i+1] = samp[1];
        }
//        System.err.println(Arrays.toString(output));
        return 0;
    }
    
}
