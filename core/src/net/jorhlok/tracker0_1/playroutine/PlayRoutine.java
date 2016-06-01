package net.jorhlok.tracker0_1.playroutine;

import net.jorhlok.tracker0_1.jorhtrackpack.*;

/**
 * This class uses tracker0_1.jorhtrackpack and jpsg to make sweet music.
 * @author jorh
 */
public class PlayRoutine {
    private static final double TwelfthRootOfTwo = Math.pow(2, 1.0/12);
    private static final int MaxCount = 1<<24;
    private static int[] StepperTable;
    
    public jtpfile data;
    public int track;
    public int frame;
    public int line;
    public int counter;
    public int counts;
    
    public ChannelModel1[] Channels1;
    /*
    I have concluded that objects inside aplay 
        cannot access their own arrays 
        but deeper is fine?
    no because I did the shit out of arrays in ChannelType1
        but PlayRoutine and JPSG aren't allowed to?
    ChannelType1 only uses primitives
        while PlayRoutune and JPSG use arrays of objects
    */
    
    public PlayRoutine() {
        Channels1 = new ChannelModel1[8];
        data = null;
        track = frame = line = counter = 0;
        counts = 15;
        if (StepperTable == null) makeTable();
    }
    
    public void update() {
        System.err.println("pr.update();");
        frame f = data.Track[track].Frame[frame];
        System.err.println("read frame");
        Channels1[0].Instrument = data.Instrument[0];
        System.err.println("read instrument");
        if (counter == 0) {
            insPattern patt = data.Track[0].InsPattern[ f.InsPattern[0] ];
            System.err.println("int note = parseNote(patt.Note[line]);");
            int note = parseNote(patt.Note[line]);
            if (note >= 0) {
                Channels1[0].Stepper = note;
                Channels1[0].Retrig = true;
            }
        }
        System.err.println("updated stuff or not " + Integer.toString(counter));
        
//        Channels1[0].step();
//        for (ChannelModel1 m : Channels1) {
//            m.step();
//        }
        
        if (++counter >= counts) {
            line = ++line%32;
            counter = 0;
            System.err.println(line);
        }
    }
    
    public int parseNote(String n) {
        if (n == null) return -1;
        String note = n.replace(':', ' ').trim();
        if (note.length() == 6) {
            int tmp = Integer.parseInt(note, 16);
            if (tmp > 0) return tmp;
        }
        if (n.equalsIgnoreCase("xxxxxx")) {
            return 0;
        }
        else if (note.length() == 3) {
            char letter = Character.toUpperCase(note.charAt(0));
            if (letter < 'A' || letter > 'G') return -1;
            byte offset;
            switch (note.charAt(1)) { //accidental
                case '-':
                case 'b':
                    offset = -1;
                    break;
                case '+':
                case '#':
                    offset = 1;
                    break;
                case ' ':
                case '=':
                    offset = 0;
                    break;
                default:
                    return -1;
            }
            byte octave = (byte)Character.digit(note.charAt(2), 16);
            if (octave < 0 || octave > 15) return -1;
            switch (letter) { //major scale is major in the world
                case 'D':
                    offset += 2;
                    break;
                case 'E':
                    offset += 4;
                    break;
                case 'F':
                    offset += 5;
                    break;
                case 'G':
                    offset += 7;
                    break;
                case 'A':
                    offset += 9;
                    break;
                case 'B':
                    offset += 11;
                    break;
                default: //'C'
            }
            return StepperTable[12*octave + offset];
        }
        return -1;
    }
    
    public static void makeTable() {
        StepperTable = new int[12*12];
        for(int i=0; i<StepperTable.length; ++i) {
            StepperTable[i] = (int)Math.round(getFreq(i)/44100*MaxCount);
        }
    }
    
    public static double getFreq(int semitone) {
        return 440*Math.pow(TwelfthRootOfTwo, semitone-9-12*4); //C=0 equals 0
    }
}
