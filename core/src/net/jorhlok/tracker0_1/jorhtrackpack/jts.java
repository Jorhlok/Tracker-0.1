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

    public float SampleRate;
    public int SamplesPerUpdate;
    public byte[] NoteUpdatePattern; //{7,8} for 8 NPS at 60 UPS
    
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
    }
    
    @Override
    public String toString() {
        //optional information
        String ret = "#Name: \"";
        if (Name != null) ret += Name;
        ret += "\"\n#By: \"";
        if (Author != null) ret += Author;
        ret += "\"\n";
        if (Comment != null) {
            ret += "#";
            ret += Comment.replaceAll("\n", "\n#") + "\n\n";
        }
        //play settings
        ret += "Settings {\n\tTargetChip = \"";
        if (TargetChip != null) ret += TargetChip;
        ret += "\"";
        ret += "\n\tLength = ";
        ret += Integer.toHexString(Length).toUpperCase();
        ret += "\n\tPatternLength = ";
        ret += Integer.toHexString(PatternLength).toUpperCase();
        ret += "\n\tSampleRate = ";
        ret += Float.toString(SampleRate); //only non-int and only decimal
        ret += "\n\tSamplesPerUpdate = ";
        ret += Integer.toHexString(SamplesPerUpdate).toUpperCase();
        ret += "\n\tNoteUpdatePattern = { ";
        if (NoteUpdatePattern != null && NoteUpdatePattern.length > 0) {
            ret += Integer.toHexString(NoteUpdatePattern[0]);
            for (int i=1; i<NoteUpdatePattern.length; ++i)
                ret += ", " + Integer.toHexString(NoteUpdatePattern[i]);
        }
        else ret += "1";
        ret += " }\n}\n\n";
        //sequence
        ret += "Track { ";
        if (Sequence != null && Sequence.length > 0) {
            ret += Integer.toHexString(Sequence[0]);
            for (int i=1; i<Sequence.length; ++i)
                ret += ", " + Integer.toHexString(Sequence[i]);
        }
        else ret += "00";
        ret += " }\n\n";
        //frames
        if (Frame != null && Frame.length > 0) {
            for (int i=0; i<Frame.length; ++i) {
                if (Frame[i] != null) {
                    if (i < 16) ret += "Frame 0";
                    else ret += "Frame ";
                    ret += Integer.toHexString(i) + Frame[i].toString() + "\n";
                }
            }
        }
        ret += "\n";
        //patterns
        if (InsPattern != null && InsPattern.length > 0) {
            for (int i=0; i<InsPattern.length; ++i) {
                if (InsPattern[i] != null) {
                    if (i < 16) ret += "Pattern 0";
                    else ret += "Pattern ";
                    ret += Integer.toHexString(i) + InsPattern[i].toString() + "\n";
                }
            }
        }
        ret += "\n";
        if (PCMPattern != null && PCMPattern.length > 0) {
            for (int i=0; i<PCMPattern.length; ++i) {
                if (PCMPattern != null) {
                    if (i < 16) ret += "PCMPattern 0";
                    else ret += "PCMPattern ";
                    ret += Integer.toHexString(i) + InsPattern[i].toString() + "\n";
                }
            }
        }
        ret += "\n";
        return ret;
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
 * Length
 * Pattern Length
 * Sample rate
 * Samples per update
 * Note update pattern
 */