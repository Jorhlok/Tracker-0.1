package net.jorhlok.tracker0_1.jorhtrackpack;

import java.util.ArrayList;
import java.util.ListIterator;

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
            ret += "\nPCMmap {\n\t" + Integer.toHexString(PCM[0]);
            for (int i=1; i<PCM.length; ++i)
                ret += ", " + Integer.toHexString(PCM[i]);
            ret += "\n\t" + Integer.toHexString(PCMLength[0]);
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
    
    public boolean fromFile(boolean hsupport, TextParser tp) {
        if (tp == null || tp.Elements == null) return false;
        for (ArrayList<String> ss : tp.Elements) {
            if (ss.size() > 2) {
                if (ss.get(0).equals("{}")) {
                    String name = ss.get(1);
                    String tmp = "";
                    ArrayList<String> array;
                    if (name.toLowerCase().endsWith("envelope")) {
                        DigitalEnvelope env;
                        switch (name.toLowerCase()) {
                            case "stereoenvelope":
                                env = EnvStereo;
                                break;
                            case "volumeenvelope":
                                env = EnvVolume;
                                break;
                            case "widthenvelope":
                                env = EnvWidth;
                                break;
                            case "noiseenvelope":
                                env = EnvNoise;
                                break;
                            default:
                                env = null;
                        }
                        if (env != null) {
                            for (ListIterator<String> iter=ss.listIterator(2);iter.hasNext();)
                                tmp += iter.next();
                            array = tp.ParseArray(tmp, ',');
                            try {
                                if (array.size()>6) {
                                    ListIterator<String> iters = array.listIterator();
                                    int max = Integer.parseInt(iters.next().trim(), 16);
                                    int min = Integer.parseInt(iters.next().trim(), 16);
                                    int nul = Integer.parseInt(iters.next().trim(), 16);
                                    int sca = Integer.parseInt(iters.next().trim(), 16);
                                    int loo = Integer.parseInt(iters.next().trim(), 16);
                                    int sus = Integer.parseInt(iters.next().trim(), 16);
                                    int end = Integer.parseInt(iters.next().trim(), 16);
                                    env.setValueBounds(max, min, nul);
                                    env.setTimeScaler(sca);
                                    env.setLoopPoint(loo);
                                    env.setSustainPoint(sus);
                                    env.setEndPoint(end);
                                    ArrayList<Integer> envlist = new ArrayList<>();
                                    for (; iters.hasNext();)
                                        envlist.add(Integer.parseInt(iters.next().trim(), 16));
                                    int[] envarr = new int[envlist.size()];
                                    for (ListIterator<Integer> iteri=envlist.listIterator(); iteri.hasNext();) {
                                        envarr[iteri.nextIndex()] = iteri.next();
                                    }
                                    env.setEnvelope(envarr);
                                }
                            } catch (Exception e) {
                                System.err.println("Error loading envelope " + name + " because:\n" + e.toString());
                            }
                        }
                    }
                    else if (name.equalsIgnoreCase("PCMmap")) {
                            if (ss.size() > 2) { //parse mapping
                                array = tp.ParseArray(ss.get(2), ',');
                                try {
                                    for (int i=0; i<16 && array.size()>=i; ++i) {
                                        PCM[i] = (short)Integer.parseInt(array.get(i).trim(), 16);
                                        if (PCM[i] < 0 || PCM[i] >= 4096) PCM[i] = 0;
                                    }
                                } catch (Exception e) {
                                    System.err.println("Error parsing " + name + " because:\n" + e.toString());
                                }
                            }
                            if (ss.size() > 3) { //parse lengths
                                array = tp.ParseArray(ss.get(3), ',');
                                try {
                                    for (int i=0; i<16 && array.size()>=i; ++i) {
                                        PCMLength[i] = (byte)Integer.parseInt(array.get(i).trim(), 16);
                                        if (PCMLength[i] == 1 || PCMLength[i] < 0 || PCMLength[i] > 64) PCMLength[i] = 0;
                                    }
                                } catch (Exception e) {
                                    System.err.println("Error parsing " + name + " because:\n" + e.toString());
                                }
                            }
                            
                    }
                    else if (name.equalsIgnoreCase("Effects")) {
                        System.err.println("Oi! You forgot to implement loading effects in jti.java!");
                    }
                }
            }
        }
        HalfSupport = hsupport;
        return true;
    }
}
