package net.jorhlok.tracker0_1.jorhtrackpack;

import java.util.ArrayList;

/**
 * Jorhlok Tracker Instrument
 * Works with ChannelType0 and ChannelType1
 * @author jorh
 */
public class jti {
    public boolean HalfSupport = false;
    
    public final DigitalEnvelope EnvStereo = new DigitalEnvelope(3,0,0);
    public final DigitalEnvelope EnvVolume = new DigitalEnvelope(15,0,0); //7,0,0 for ChannelType0
    public final DigitalEnvelope EnvWidth = new DigitalEnvelope(15,0,0); // N/A for ChannelType0
    public final DigitalEnvelope EnvNoise = new DigitalEnvelope((1<<24)-1,0,0); // N/A for ChannelType0
    public short[] PCM; // 0-FF or 0-FFF
    public byte[] PCMLength; //0-3FF (1023) or 0-40 (64)
        //ChannelType0 PCMLength map Start, Loop Begin, Loop End, End
    
    // N/A to ChannelType0
    public char[] Effect; // Alphanumeric + Punctuation 20-7E (32-126)
    public byte[] FX1; // 0-F
    public byte[] FX2; // 0-F
    public short FXLength; // 0-10 (16)
    
    public jti() {
        PCM = new short[16]; //0-4095
        for (int i=0; i<PCM.length; ++i) { PCM[i] = -1; }
        PCMLength = new byte[16];
        for (int i=0; i<PCMLength.length; ++i) { PCMLength[i] = -1; }
        Effect = new char[16];
        for (int i=0; i<Effect.length; ++i) { Effect[i] = ' '; }
        FX1 = new byte[Effect.length];
        for (int i=0; i<FX1.length; ++i) { FX1[i] = -1; }
        FX2 = new byte[Effect.length];
        for (int i=0; i<FX2.length; ++i) { FX2[i] = -1; }
        FXLength = 0;
    }
    
    @Override
    public String toString() {
        String ret = "";
        String str;
        int j, k;
        str = EnvStereo.toString();
        j = str.indexOf('\n');
        k = str.lastIndexOf('\n');
        ret += "StereoEnvelope {\n" + str.substring(j, k+1) + "}\n";
        str = EnvVolume.toString();
        j = str.indexOf('\n');
        k = str.lastIndexOf('\n');
        ret += "\nVolumeEnvelope {\n" + str.substring(j, k+1) + "}\n";
        if (!HalfSupport) {
            str = EnvWidth.toString();
            j = str.indexOf('\n');
            k = str.lastIndexOf('\n');
            ret += "\nWidthEnvelope {\n" + str.substring(j, k+1) + "}\n";
            str = EnvNoise.toString();
            j = str.indexOf('\n');
            k = str.lastIndexOf('\n');
            ret += "\nNoiseEnvelope {\n" + str.substring(j, k+1) + "}\n";
            ret += "\nPCMmap {\n" + Integer.toHexString(PCM[0]);
            for (int i=1; i<PCM.length; ++i)
                ret += ", " + Integer.toHexString(PCM[i]);
            ret += "\n" + Integer.toHexString(PCMLength[0]);
            for (int i=1; i<PCMLength.length; ++i)
                ret += ", " + Integer.toHexString(PCMLength[i]);
            ret += "\n}\n";
            ret += "\nEffects {\n";
            for (int i=0; i<Effect.length; ++i) {
                ret += Effect[i];
                if (FX1[i] >= 0 && FX1[i] < 16) ret += Integer.toHexString(FX1[i]);
                else ret += '/';
                if (FX2[i] >= 0 && FX2[i] < 16) ret += Integer.toHexString(FX2[i]);
                else ret += '/';
                ret += '\n';
            }
            ret += "}\n";
        }
        else {
            ret += "PCMmap {\n" + Integer.toHexString(PCM[0]) + "\n" + Integer.toHexString(PCMLength[0]);
            for (int i=1; i<4; ++i)
                ret += ", " + Integer.toHexString(PCMLength[i]);
            ret += "\n}\n";
        }
        return ret;
    }
    
    public boolean fromFile(boolean hsupport, ArrayList< ArrayList<String> > str) {
        return false;
    }
}
