package net.jorhlok.tracker0_1.jorhtrackpack;

import java.util.ArrayList;
import java.util.ListIterator;

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
        String ret;
        if (InsPattern[0] >= 0 && InsPattern[0] < 256) ret = "{ " + Integer.toHexString(InsPattern[0]);
        else ret = "{ -";
        for (int i=1; i<InsPattern.length; ++i) {
            if (InsPattern[i] >= 0 && InsPattern[i] < 256) ret += " ," + Integer.toHexString(InsPattern[i]);
            else ret += " ,-";
        }
        if (PCMPattern >= 0 && PCMPattern < 256) ret += " ," + Integer.toHexString(PCMPattern) + "}";
        else ret += " ,-}";
        return ret;
    }
    
    public boolean fromFile(ArrayList<String> str) {
        if (str == null) return false;
        String tmp = "";
        for (ListIterator<String> iter=str.listIterator(); iter.hasNext();)
            tmp += iter.next();
        ArrayList<String> arr = TextParser.ParseArray(tmp, ',');
        ListIterator<String> iter = arr.listIterator();
        for (int i=0; i<InsPattern.length && iter.hasNext(); ++i)
            try {
                InsPattern[i] = (short)Integer.parseInt(iter.next(), 16);
                if (InsPattern[i] > 255) InsPattern[i] = -1;
            } catch (Exception e) {
                InsPattern[i] = -1;
            }
        
        try {
            PCMPattern = (short)Integer.parseInt(iter.next(), 16);
            if (PCMPattern > 255) PCMPattern = -1;
        } catch (Exception e) {
            PCMPattern = -1;
        }
        return true;
    }
}
