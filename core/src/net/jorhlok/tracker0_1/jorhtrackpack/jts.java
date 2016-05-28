package net.jorhlok.tracker0_1.jorhtrackpack;

/**
 * Jorhlok Track Sequence
 * @author jorh
 */
public class jts {
    public String Name;
    public String Author;
    public String Comment;
    
    public short Length; // 0-FF
    public short[] Sequence; // 0-FF (arrangement of frames in the track)
    public frame[] Frame;
    public insPattern[] InsPattern;
    public pcmPattern[] PCMPattern;
    public byte[][] PCM8; // points to owner's copy
    public jti[] Instrument; // points to owner's copy
    
    public String TargetChip;
    public short PatternLength; //0-FF
    public short UpdatesPerSecond; // 0-FF updates to the chip per second
    public boolean NPSorUPN; // below is Notes Per Second (false) or Updates Per Note (true)
    public short NoteFrequency; // 0-FF
    
    public jts() {
        Name = Author = Comment = null;
        
        Length = 0;
        Sequence = new short[256];
        for (short i=0; i<256; ++i) { Sequence[i] = -1; }
        Frame = new frame[256];
        InsPattern = new insPattern[256];
        PCMPattern = new pcmPattern[256];
        
        TargetChip = "JPSG1609S"; //all the bells and whistles by default
        PatternLength = 32;
        UpdatesPerSecond = 60;
        NPSorUPN = false;
        NoteFrequency = 8;
    }
    
    @Override
    public String toString() {
        return null;
    }

    public void newInsPattern(int i) {
        if (i >= 0 && i< 256) {
            InsPattern[i] = new insPattern();
            InsPattern[i].Length = PatternLength;
        }
    }

    public void newPCMPattern(int i) {
        if (i >= 0 && i< 256) {
            PCMPattern[i] = new pcmPattern();
            PCMPattern[i].Length = PatternLength;
        }
    }

    public void newFrame(int i) {
        if (i >= 0 && i< Length) {
            Frame[i] = new frame();
        }
    }
}

/**
 * Track attributes (0-255) (XY.jts) (jorhlok track sequences)
 * =====================
 * Name
 * Author
 * Comment
 * 
 * Target chip
 * Pattern Length
 * Updates per Second
 * Notes per Second or Updates per Note
 */