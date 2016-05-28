package net.jorhlok.tracker0_1.jorhtrackpack;

/**
 * Jorhlok Tracker Instrument
 * @author jorh
 */
public class jti {
    public byte[] SEnvelope; // 0-3
    public byte[] VEnvelope; // 0-F
    public byte[] WEnvelope; // 0-F
    public short SScaler; // 0-FF
    public short VScaler; // 0-FF
    public short WScaler; // 0-FF
    public short SSustain; // 0-FF
    public short VSustain; // 0-FF
    public short WSustain; // 0-FF
    public short SLength; // 0-FF
    public short VLength; // 0-FF
    public short WLength; // 0-FF
    public short SLoop; // 0-FF
    public short VLoop; // 0-FF
    public short WLoop; // 0-FF
    public short[] PCM4; // 0-FFF
    public byte[] PCMLength; //0-40 (64)
    public char[] Effect; // Alphanumeric + Punctuation 20-7E (32-126)
    public byte[] FX1; // 0-F
    public byte[] FX2; // 0-F
    public short FXLength; // 0-10 (16)
    
    public jti() {
        SEnvelope = new byte[256];
        for (byte b : SEnvelope) { b = 3; }
        VEnvelope = new byte[256];
        for (byte b : VEnvelope) { b = 15; }
        WEnvelope = new byte[256];
        for (byte b : WEnvelope) { b = 15; }
        SSustain = 255;
        VSustain = 255;
        WSustain = 255;
        SLoop = 255;
        VLoop = 255;
        WLoop = 255;
        SLength = 0;
        VLength = 0;
        WLength = 0;
        PCM4 = new short[16]; //0-4095
        for (short s : PCM4) { s = -1; }
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
