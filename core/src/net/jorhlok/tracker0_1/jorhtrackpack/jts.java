package net.jorhlok.tracker0_1.jorhtrackpack;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Jorhlok Track Sequence
 * @author jorh
 */
public class jts {
    public String Name;
    public String Author;
    public String Comment;
    
    public short Length; // 0-FF
    public short Loop; // 0-FF
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
        ret += "\n\tLoop = ";
        ret += Integer.toHexString(Loop).toUpperCase();
        ret += "\n\tPatternLength = ";
        ret += Integer.toHexString(PatternLength).toUpperCase();
        ret += "\n\tSampleRate = ";
        ret += Float.toString(SampleRate); //only non-int and only decimal
        ret += "\n\tSamplesPerUpdate = ";
        ret += Integer.toHexString(SamplesPerUpdate).toUpperCase();
        ret += "\n\tNoteUpdatePattern = ";
        if (NoteUpdatePattern != null && NoteUpdatePattern.length > 0) {
            ret += Integer.toHexString(NoteUpdatePattern[0]);
            for (int i=1; i<NoteUpdatePattern.length; ++i)
                ret += ", " + Integer.toHexString(NoteUpdatePattern[i]);
        }
        else ret += "1";
        ret += "\n}\n\n";
        //sequence
        ret += "Track { ";
        if (Sequence != null && Sequence.length > 0) {
            ret += Integer.toHexString(Sequence[0]);
            for (int i=1; i<Sequence.length; ++i) {
                if (Sequence[i] < 0 || Sequence[i] > 255) break;
                ret += ", " + Integer.toHexString(Sequence[i]);
            }
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
                if (PCMPattern[i] != null) {
                    if (i < 16) ret += "PCMPattern 0";
                    else ret += "PCMPattern ";
                    System.err.println(i);
                    ret += Integer.toHexString(i) + PCMPattern[i].toString() + "\n";
                }
            }
        }
        ret += "\n";
        return ret;
    }
    
    public boolean fromFile(TextParser tp) {
        if (tp == null || tp.Elements == null) return false;
        int sectionpast = 0; 
        ArrayList<ArrayList<String>> frames = new ArrayList<>();
        ArrayList<ArrayList<String>> pats = new ArrayList<>();
        ArrayList<ArrayList<String>> pcmpats = new ArrayList<>();
        for (ArrayList<String> ss : tp.Elements) {
            if (ss.size() > 2) {
                ListIterator<String> iter = ss.listIterator();
                String prefix = iter.next();
                if (prefix.equals("{}")) {
                    sectionpast = 3;
                    String name = iter.next();
                    if (name.equalsIgnoreCase("settings")) {
                        while (iter.hasNext()) {
                            String[] variable = TextParser.ParseVar(iter.next(), "=:");
                            if (variable != null && variable.length > 2) {
                                switch (variable[0].toLowerCase()) {
                                    case "targetchip":
                                        if (variable[2].startsWith("\"") && variable[2].endsWith("\"")) //remove excess quotes
                                            variable[2] = variable[2].substring(1, variable[2].length()-1);
                                        TargetChip = variable[2];
                                        break;
                                    case "length":
                                        try {
                                            Length = (short)Integer.parseInt(variable[2], 16);
                                        } catch (Exception e) {
                                            System.err.println("Error reading " + variable[0] + " because:\n" + e.toString());
                                            Thread.dumpStack();
                                        }
                                        break;
                                    case "patternlength":
                                        try {
                                            PatternLength = (short)Integer.parseInt(variable[2], 16);
                                        } catch (Exception e) {
                                            System.err.println("Error reading " + variable[0] + " because:\n" + e.toString());
                                            Thread.dumpStack();
                                        }
                                        break;
                                    case "samplerate":
                                        try {
                                            SampleRate = Float.parseFloat(variable[2]);
                                        } catch (Exception e) {
                                            System.err.println("Error reading " + variable[0] + " because:\n" + e.toString());
                                            Thread.dumpStack();
                                        }
                                        break;
                                    case "samplesperupdate":
                                        try {
                                            SamplesPerUpdate = Integer.parseInt(variable[2]);
                                        } catch (Exception e) {
                                            System.err.println("Error reading " + variable[0] + " because:\n" + e.toString());
                                            Thread.dumpStack();
                                        }
                                        break;
                                    case "noteupdatepattern":
                                        try {
                                            ArrayList<String> arr = TextParser.ParseArray(variable[2], ',');
                                            NoteUpdatePattern = new byte[arr.size()];
                                            for (ListIterator<String> iter2=arr.listIterator(); iter2.hasNext();)
                                                NoteUpdatePattern[iter2.nextIndex()] = (byte)Integer.parseInt(iter2.next(), 16);
                                        } catch (Exception e) {
                                            System.err.println("Error reading " + variable[0] + " because:\n" + e.toString());
                                            Thread.dumpStack();
                                        }
                                        break;
                                }
                            }
                        }

                    }
                    else if (name.equalsIgnoreCase("track")){
                        String tmp = "";
                        while (iter.hasNext())
                            tmp += iter.next();
                        try {
                            ArrayList<String> arr = TextParser.ParseArray(tmp, ',');
                            for (ListIterator<String> iter2=arr.listIterator(); iter2.hasNext();)
                                Sequence[iter2.nextIndex()] = (short)Integer.parseInt(iter2.next(), 16);
                        } catch (Exception e) {
                            System.err.println("Error reading " + name + " because:\n" + e.toString());
                            Thread.dumpStack();
                        }
                    }
                    else if (name.toLowerCase().startsWith("frame")) {
                        String[] id = TextParser.ParseVar(name, " ");
                        if (id != null && id.length > 2) {
                            int index;
                            try {
                                index = Integer.parseInt(id[2], 16);
                            } catch (Exception e) {
                                System.err.println("Error reading " + name + " because:\n" + e.toString());
                                Thread.dumpStack();
                                index = -1;
                            }
                            if (index >= 0 && index < 256) {
                                frames.add((ArrayList<String>)ss.clone());
                            }
                        }
                    }
                    else if (name.toLowerCase().startsWith("pattern")) {
                        String[] id = TextParser.ParseVar(name, " ");
                        if (id != null && id.length > 2) {
                            int index;
                            try {
                                index = Integer.parseInt(id[2], 16);
                            } catch (Exception e) {
                                System.err.println("Error reading " + name + " because:\n" + e.toString());
                                Thread.dumpStack();
                                index = -1;
                            }
                            if (index >= 0 && index < 256) {
                                pats.add((ArrayList<String>)ss.clone());
                            }
                        }
                    }
                    else if (name.toLowerCase().startsWith("pcmpattern")) {
                        String[] id = TextParser.ParseVar(name, " ");
                        if (id != null && id.length > 2) {
                            int index;
                            try {
                                index = Integer.parseInt(id[2], 16);
                            } catch (Exception e) {
                                System.err.println("Error reading " + name + " because:\n" + e.toString());
                                Thread.dumpStack();
                                index = -1;
                            }
                            if (index >= 0 && index < 256) {
                                pcmpats.add((ArrayList<String>)ss.clone());
                            }
                        }
                    }
                }
                else if (prefix.equals("#")) {
                    String tmp = iter.next();
                    String[] variable = TextParser.ParseVar(tmp, prefix.substring(1, 1));
                    if (variable != null && variable.length > 2) {
                        if (variable[2].startsWith("\"") && variable[2].endsWith("\"")) //remove excess quotes
                            variable[2] = variable[2].substring(1, variable[2].length()-1);
                        switch (variable[0].toLowerCase()) {
                            case "name":
                            case "title":
                                if (sectionpast < 1) sectionpast = 1;
                                Name = variable[2];
                                break;
                            case "by":
                            case "author":
                                if (sectionpast < 2) sectionpast = 2;
                                Author = variable[2];
                                break;
                            default:
                                //comment is suppose to be between #name\n#author and everything else
                                if (sectionpast == 2) Comment += tmp + "\n";
                        }
                    }
                }
            }
        }
        //The behavior depends on the Settings {} section so has to be done after
        for (ArrayList<String> ss : frames) {
            //already did the checking so just get it done
            String[] id = TextParser.ParseVar(ss.get(1), " ");
            ss.remove(1);
            ss.remove(0);
            int index = Integer.parseInt(id[2], 16);
            newFrame(index); //reset Frame
            Frame[index].fromFile(ss);
        }
        for (ArrayList<String> ss : pats) {
            //already did the checking so just get it done
            String[] id = TextParser.ParseVar(ss.get(1), " ");
            ss.remove(1);
            ss.remove(0);
            int index = Integer.parseInt(id[2], 16);
            newInsPattern(index); //reset InsPattern
            InsPattern[index].fromFile(ss);
        }
        for (ArrayList<String> ss : pcmpats) {
            //already did the checking so just get it done
            String[] id = TextParser.ParseVar(ss.get(1), " ");
            ss.remove(1);
            ss.remove(0);
            int index = Integer.parseInt(id[2], 16);
            newPCMPattern(index); //reset PCMPattern
            PCMPattern[index].fromFile(ss);
        }

        return true;
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