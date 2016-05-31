package net.jorhlok.tracker0_1.playroutine;

import net.jorhlok.tracker0_1.jorhtrackpack.jti;

/**
 * This is the conceptual model of a ChannelType1 inside the PlayRoutine.
 * Bridge between sound chip and the track.
 * @author jorh
 */
public class ChannelModel1 {
    public jti Instrument;
    public boolean Retrig = false;
    public boolean Release = false;
    public int Stepper = 0;
    public int Note = 0; // 0 to 12^5-1 or 248831
    public byte Stereo = 3;
    public byte Volume = 15;
    public byte Width = 15;
    public short EnvelopeIndex = 0;
    public char Effect = ' '; // Alphanumeric + Punctuation 20-7E
    public byte FX1 = 0; // 0-F (-1 for null)
    public byte FX2 = 0; // 0-F (-1 for null)
    public int FXIndex = 0;
    
    public short SEnvIndex = 0;
    public short VEnvIndex = 0;
    public short WEnvIndex = 0;
    
//vibrato and pitch slide
    public int VibLo;
    public int VibMi;
    public int VibHi;
    public byte VibIndex;
    public byte VibStepper;
    public byte VibMode;
    
//tremolo and volume slide
    public byte VolLo;
    public byte VolHi;
    public byte VolIndex;
    public byte VolCounter;
    public byte VolStepper;
    
//pulse width cycle and slide
    public byte WLo;
    public byte WHi;
    public byte WIndex;
    public byte WCounter;
    public byte WStepper;
    
    
    public void step() {
        
    }
    
    public int getStepper() {
        return 0;
    }
    
    public byte getStereo() {
        return 0;
    }
    
    public byte getVolume() {
        return 0;
    }
    
    public byte getWidth() {
        return 0;
    }
    
    public boolean getNoise() {
        return false;
    }
    
    public byte[] getSamples() {
        return null;
    }
    
    public byte getNSamples() {
        return 0;
    }
}
