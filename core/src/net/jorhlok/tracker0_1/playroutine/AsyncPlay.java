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
    public JPSG chip = new JPSG();
    public short[] output;
    public int buffer = 44100/60;
    public ChannelType1 ch = new ChannelType1();
    
    @Override
    public Object call() {
        return use();
    }

    public Object use() {
        System.err.println("aplay.call();");
        if (chip == null || pr == null) return -1;
//        System.err.println(Arrays.toString(ch.getSamples()));
//        pr.update();
//        System.err.println("pr.update(); //done");
//        chip.setStepper( 0, (int)Math.round(440.0/44100*(1<<24) ) );//pr.Channels1[0].Stepper);
//        ch.setStepper( (int)Math.round(440.0/44100*(1<<24) ) );//pr.Channels1[0].Stepper);
        ch.setStepper(pr.parseNote(pr.data.Track[0].InsPattern[0].Note[0]));
//        chip.setStepper(0,0x040000);
//        System.err.println("setstep");
//        chip.setSamples(0, pr.data.PCM4[15]);
        ch.setSamples(pr.data.PCM4[Math.max(0, pr.data.Track[0].InsPattern[0].Volume[0] )]);
//        System.err.println("setsamp");
//        chip.setnSamples(0, 2);
        ch.setnSamples(2);
//        System.err.println("setn");
//        chip.setStereo(0, new int[] {3,-1,-1,-1 ,-1,-1,-1,-1});
//        System.err.println("sets");
//        if (ch.getWidth() <= 0) ch.setWidth(15);
//        else ch.setWidth(ch.getWidth()-1);
//        if (chip.getWidth(0) <= 0) chip.setWidth(0,15);
//        else chip.setWidth(0,chip.getWidth(0)-1);
        ch.setWidth(pr.data.Track[0].InsPattern[0].Width[0]);
//        System.err.println("setw");
//        ch.setNoise(true);
//        chip.setNoise(new int[] {1,-1,-1,-1 ,-1,-1,-1,-1});
//        System.err.println("setnz");


        
        output = new short[buffer*2];
//        System.err.println("output = new short[buffer*2];");
        short[] samp;
        for (int i=0; i<output.length; i+=2) {
//            chip.step();
//            samp = chip.output();
            ch.step();
            samp = new short[2];
            samp[0] = samp[1] = (short)(ch.output()*2000);
            if (samp == null || samp.length != 2) return -2;
            output[i] = samp[0];
            output[i+1] = samp[1];
        }
//        System.err.println(Arrays.toString(output));
        return 0;
    }
    
}
