package net.jorhlok.tracker0_1.jorhtrackpack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

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
    
    public String Path;
    
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
        String ret = "#JTP version: \"";
        if (RecognizedVersions != null && RecognizedVersions[0] != null) ret += RecognizedVersions[0];
        ret += "\"\n#Name: \"";
        if (Name != null) ret += Name; 
        ret += "\"\n#Author: \"";
        if (Author != null) ret += Author + "\"\n";
        else ret += "\"\n";
        if (Comment != null) {
            ret += "#";
            ret += Comment.replaceAll("\n", "\n#") + "\n\n";
        }
        if (RecognizedDescriptions != null && RecognizedDescriptions[0] != null) ret += "\n" + RecognizedDescriptions[0];
        ret += "\n";
        return ret;
    }
    
    public boolean PCM4FromFile(int index, TextParser tp) {
        if (index < 0 || index > 4095 || tp == null || tp.Elements == null) return false;
        ArrayList<Byte> b = new ArrayList<>();
        for (ArrayList<String> ss : tp.Elements) {
            if (ss != null)for (String s : ss) {
                if (s != null)for (int i=0; i<s.length(); ++i) {
                    int n = Character.digit(s.charAt(i), 16);
                    if (n >= 0) b.add((byte)(n-8));
                }
            }
        }
        if(b.isEmpty()) return false;
        PCM4[index] = new byte[b.size()];
        for (ListIterator<Byte> iter=b.listIterator(); iter.hasNext();)
            PCM4[index][iter.nextIndex()] = iter.next();
        return true;
    }
    
    public boolean PCM8FromFile(int index, TextParser tp) {
        if (index < 0 || index > 255 || tp == null || tp.Elements == null) return false;
        ArrayList<Byte> b = new ArrayList<>();
        for (ArrayList<String> ss : tp.Elements) {
            if (ss != null)for (String s : ss) {
                if (s != null)for (int i=0; i<s.length(); ++i) {
                    int n = Character.digit(s.charAt(i), 16);
                    if (n >= 0) b.add((byte)(n));
                }
            }
        }
        if(b.isEmpty()) return false;
        PCM8[index] = new byte[b.size()/2];
        for (ListIterator<Byte> iter=b.listIterator(); iter.hasNext();) {
            byte high = iter.next(); //high byte
            if (iter.hasNext()) PCM8[index][iter.nextIndex()] = (byte)(iter.next() + 16*high - 128);
        }
        return true;
    }
    
    public boolean readmeFromFile(TextParser tp) {
        return false;
    }
    
    public boolean save() {
        ZipOutputStream zout = null;
        try {
            File file = new File(Path);
            zout = new ZipOutputStream(new FileOutputStream(file));
            zout.setLevel(9);
            ZipEntry entry;
            //write reamde.txt
            entry = new ZipEntry("readme.txt");
            zout.putNextEntry(entry);
            zout.write(strToBytes(readmeString()));
            zout.closeEntry();
            //write jts
            if (Track != null)
                for (int i=0; i<Track.length; ++i) {
                    if (Track[i] != null) {
                        String str = Integer.toHexString(i);
                        if (str.length() < 2) str = "0" + str;
                        entry = new ZipEntry(str + ".jts");
                        zout.putNextEntry(entry);
                        zout.write(strToBytes(Track[i].toString()));
                        zout.closeEntry();
                    }
                }
            //write ji0
            if (InsType0 != null) 
                for (int i=0; i<InsType0.length; ++i) {
                    if (InsType0[i] != null) {
                        String str = Integer.toHexString(i);
                        if (str.length() < 2) str = "0" + str;
                        entry = new ZipEntry(str + ".ji0");
                        zout.putNextEntry(entry);
                        zout.write(strToBytes(InsType0[i].toString()));
                        zout.closeEntry();
                    }
                }
            //write ji1
            if (InsType1 != null) 
                for (int i=0; i<InsType1.length; ++i) {
                    if (InsType1[i] != null) {
                        String str = Integer.toHexString(i);
                        if (str.length() < 2) str = "0" + str;
                        entry = new ZipEntry(str + ".ji1");
                        zout.putNextEntry(entry);
                        zout.write(strToBytes(InsType1[i].toString()));
                        zout.closeEntry();
                    }
                }
            //write hp4
            if (PCM4 != null) 
                for (int i=0; i<PCM4.length; ++i) {
                    if (PCM4[i] != null) {
                        String str = Integer.toHexString(i);
                        while (str.length() < 3) str = "0" + str;
                        entry = new ZipEntry(str + ".hp4");
                        zout.putNextEntry(entry);
                        zout.write(strToBytes(PCM4ToString(i)));
                        zout.closeEntry();
                    }
                }
            //write hp8
            if (PCM8 != null) 
                for (int i=0; i<PCM8.length; ++i) {
                    if (PCM8[i] != null) {
                        String str = Integer.toHexString(i);
                        if (str.length() < 2) str = "0" + str;
                        entry = new ZipEntry(str + ".hp8");
                        zout.putNextEntry(entry);
                        zout.write(strToBytes(PCM8ToString(i)));
                        zout.closeEntry();
                    }
                }
            //close zip file
            zout.close();
        } catch (Exception e) {
            try {
                if (zout != null ) zout.close();
            } catch (Exception ee) {
                //nothing
            }
            System.err.println("Exception trying to save jtp file. " + e.toString());
            Thread.dumpStack();
            return false;
        }
        return true;
    }
    
    public boolean load() {
        TextParser tp = new TextParser();
        ZipInputStream zin = null;
        String str;
        String name;
        int i;
        byte[] b = new byte[256];
        try {
            File file = new File(Path);
            zin = new ZipInputStream(new FileInputStream(file));
            ZipEntry ze;
            while ((ze = zin.getNextEntry()) != null) {
                name = ze.getName();
                System.out.println(name);
                str = "";
                while ((i = zin.read(b)) > 0) {
                    for (int j=0; j<i; ++j)
                        str += (char)b[j];
                }
                tp.Parse(str);
                //System.out.println(tp.Elements.toString());
//                for (ArrayList<String> e : tp.Elements)
//                    System.out.println(e.toString());
                if (name.length() > 4) {
                    int num;
                    try {
                        num = Integer.parseInt(name.substring(0, name.length()-4),16);
                    } catch (Exception e) {
                        num = -1;
                    }
                    boolean result = false;
                    if (name.equalsIgnoreCase("readme.txt") ) {
                        result = readmeFromFile(tp);
                    }
                    else if (num >= 0 && num < 4096 && name.substring(name.length()-3).equalsIgnoreCase("hp4") ) {
                        if (PCM4[num] == null) PCM4[num] = new byte[64];
                        result = PCM4FromFile(num, tp);
                    }
                    else if (num >= 0 && num < 256 && name.substring(name.length()-3).equalsIgnoreCase("hp8") ) {
                        if (PCM4[num] == null) PCM4[num] = new byte[256];
                        result = PCM8FromFile(num, tp);
                    }
                    else if (num >= 0 && num < 256 && name.substring(name.length()-3).equalsIgnoreCase("ji0") ) {
                        if (InsType0[num] == null) newInsType0(num);
                        result = InsType0[num].fromFile(true, tp);
                    }
                    else if (num >= 0 && num < 256 && name.substring(name.length()-3).equalsIgnoreCase("ji1") ) {
                        if (InsType1[num] == null) newInsType1(num);
                        result = InsType1[num].fromFile(false, tp);
                    }
                    else if (num >= 0 && num < 256 && name.substring(name.length()-3).equalsIgnoreCase("jts") ) {
                        //newTrack(num);
                        result = Track[num].fromFile(tp);
                    }
                    
                    if (!result) System.err.println("Error reading " + name);
                }
            }
            zin.close();
        } catch (Exception e) {
            try {
                if (zin != null ) zin.close();
            } catch (Exception ee) {
                //nothing
            }
            System.err.println("Exception trying to load jtp file. " + e.toString());
            Thread.dumpStack();
            return false;
        }
        return true;
    }
    
    public byte[] strToBytes(String str) {
        if (str == null) return null;
        byte[] barr = new byte[str.length()];
        for (int i=0; i<str.length(); ++i)
            barr[i] = (byte)str.charAt(i);
        return barr;
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