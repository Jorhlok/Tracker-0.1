package net.jorhlok.tracker0_1.jorhtrackpack;

/**
 * Jorhlok Track Pack File
 * @author jorh
 */
public class jtpfile {
    public String Name;
    public String Author;
    public String Comment;
    public jts[] Track;
    public byte[][] PCM4; // -8 to 7
    public byte[][] PCM8; // -128 to 127
    public jti[] Instrument;
    
    public jtpfile() {
        Name = Author = Comment = null;
        Track = new jts[256];
        PCM4 = new byte[4096][];
        PCM8 = new byte[256][];
        Instrument = new jti[256];
    }

    public void newTrack(int i) {
        if (i >= 0 && i < 256) {
            Track[i] = new jts();
        }
    }
}

/**
 * Track pack file structure (inside a zip file) (abcdef.jtp) (jorhlok track pack)
 * =========================
 * readme.txt       (contains global pack attributes plus an optional description of what the file format is)
 * 00.jts           (first track with its own frames/patterns)
 * 01.jts           (second track with its own...)
 * ...
 * 0a.jts           (tenth track...)
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