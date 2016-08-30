package net.jorhlok.tracker0_1.jorhtrackpack;

/**
 * Jorhlok Tracker Instrument
 * Works with ChannelType0 and ChannelType1
 * @author jorh
 */
public class jti {
    //try making an envelope class
    public final DigitalEnvelope EnvStereo = new DigitalEnvelope(3,0,0);
    public final DigitalEnvelope EnvVolume = new DigitalEnvelope(15,0,0); //7,0,0 for ChannelType0
    public final DigitalEnvelope EnvWidth = new DigitalEnvelope(15,0,0); // N/A for ChannelType0
    public final DigitalEnvelope EnvNoise = new DigitalEnvelope((1<<24)-1,0,0); // N/A for ChannelType0
    public short[] PCM; // 0-FF or 0-FFF
    public byte[] PCMLength; //0-3FF (1023) or 0-40 (64)
    
    // N/A to ChannelType0
    public char[] Effect; // Alphanumeric + Punctuation 20-7E (32-126)
    public byte[] FX1; // 0-F
    public byte[] FX2; // 0-F
    public short FXLength; // 0-10 (16)
    
    public jti() {
        PCM = new short[16]; //0-4095
        for (short s : PCM) { s = -1; }
        PCMLength = new byte[16];
        for (byte b : PCMLength) { b = 0; }
        Effect = new char[16];
        for (char c : Effect) { c = 0; }
        FX1 = new byte[16];
        for (byte b : FX1) { b = -1; }
        FX2 = new byte[16];
        for (byte b : FX2) { b = -1; }
        FXLength = 0;
    }
    
    @Override
    public String toString() {
        return null;
    }
}
