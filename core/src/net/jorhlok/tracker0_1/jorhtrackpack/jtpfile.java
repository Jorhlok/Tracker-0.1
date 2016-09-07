package net.jorhlok.tracker0_1.jorhtrackpack;

/**
 * Jorhlok Track Pack File
 * @author jorh
 */
public class jtpfile {
    public String Name;
    public String Author;
    public String Comment;
    public final String[] RecognizedVersions = {
            //"0.2",
            "0.1"
        };
    public final String[] RecognizedDescriptions = {
        //"",
        "This Jorh Track Pack file is zipped up instructions to make sweet music on a custom software synthesizer.\nMore info at jorhlok.net\n"
    };
    
    public jts[] Track;
    public byte[][] PCM4; // -8 to 7
    public byte[][] PCM8; // -128 to 127
    public jti[] InsType0;
    public jti[] InsType1;
    
    public jtpfile() {
        Name = Author = Comment = null;
        Track = new jts[256];
        PCM4 = new byte[4096][];
        PCM8 = new byte[256][];
        InsType0 = new jti[256];
        InsType1 = new jti[256];
    }

    public void newTrack(int i) {
        if (i >= 0 && i < 256) {
            Track[i] = new jts();
        }
    }
    
    public void newInsType0(int i) {
        if (i >= 0 && i < 256) {
            InsType0[i] = new jti();
            InsType0[i].EnvVolume.setValueBounds(7, 0, 0);
            InsType0[i].HalfSupport = true;
        }
    }
    public void newInsType1(int i) {
        if (i >= 0 && i < 256) {
            InsType1[i] = new jti();
        }
    }
    
    public String PCM4ToString(int index) {
        if (index < 0 || index > 255 || PCM4[index] == null || PCM4[index].length < 1) return "";
        String ret = "";
        String str;
        for (int i=0; i<PCM4[index].length; ++i) {
            str = Integer.toHexString(PCM4[index][i]+8);
            ret += str.charAt(str.length()-1);
        }
        return ret;
    }
    
    public String PCM8ToString(int index) {
        if (index < 0 || index > 255 || PCM8[index] == null || PCM8[index].length < 1) return "";
        String ret = "";
        String str;
        for (int i=0; i<PCM8[index].length; ++i) {
            str = Integer.toHexString(PCM8[index][i]+128);
            while (str.length() < 2) str = "0" + str;
            ret += str.substring(str.length()-2);
        }
        return ret;
    }
    
    public String readmeString() {
        String ret = "JTP version: \"" + RecognizedVersions[0]
                + "\"\nName: \"" + Name 
                + "\"\nAuthor: \"" + Author
                + "\"\n" + Comment
                + "\n\n\n" + RecognizedDescriptions[0] + "\n";
        return ret;
    }
}

/**
 * Track pack file structure (inside a zip file) (abcdef.jtp) (jorhlok track pack)
 * =========================
 * readme.txt       (contains global pack attributes plus an optional description of what the file format is)
 * 00.jts           (first track with its own frames/patterns)
 * 01.jts           (second track with its own...)
 * ...
 * 0A.jts           (tenth track...)
 * ...
 * 000.hp4          (first raw 4-bit pcm in ascii hexadecimal for an instrument)
 * 001.hp4
 * ...
 * 00.hp8           (first raw 8-bit pcm in ascii big-endian hexadecimal for drums or other samples)
 * 01.hp8
 * ...
 * 00.ji0           (first ChannelType0 instrument data including desired samples and optional envelopes and intrinsic effects)
 * 01.ji0
 * ...
 * 00.ji1           (first ChannelType1 instrument data including desired samples and optional envelopes and intrinsic effects)
 * 01.ji1
 * ...
 */