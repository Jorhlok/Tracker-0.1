package net.jorhlok.tracker0_1.jorhtrackpack;

/**
 * frame of patterns
 * @author jorh
 */
public class frame {
    public short[] InsPattern; // 8 of these
    public short PCMPattern;
    
    public frame() {
        InsPattern = new short[8];
        for (short i=0; i<8; ++i) { InsPattern[i] = -1; }
        PCMPattern = -1;
    }
    
    @Override
    public String toString() {
        return null;
    }
}
