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
    public byte playmode; //0-stopped 1-reset 2-playing 
    
    public ChannelModel1[] Channels1;
    private ChannelModel1[] PrChan1;
    
    public PlayRoutine() {
        Channels1 = new ChannelModel1[8];
        PrChan1 = new ChannelModel1[8];
        for (int i=0; i<Channels1.length; ++i) {
            Channels1[i] = new ChannelModel1();
            PrChan1[i] = new ChannelModel1();
        }
        data = null;
        track = frame = line = counter = counts = playmode = 0;
        if (StepperTable == null) makeTable();
    }
    
    public void update() {
        byte playing = playmode;
        if (playing == 2) {
            for (int i=0; i<PrChan1.length; ++i)
                if (PrChan1[i].Retrig) PrChan1[i].Retrig = Channels1[i].Retrig;
            frame f = data.Track[track].Frame[data.Track[track].Sequence[frame]];
            
            if (counter == 0) {
                if (++counts >= data.Track[track].NoteUpdatePattern.length)
                    counts = 0;
                
                for (int i=0; i<PrChan1.length; ++i) try {
                    insPattern patt = data.Track[0].InsPattern[ f.InsPattern[i] ];
                    int tmp = parseNote(patt.Note[line]);
                    if (tmp >= 0) {
                        PrChan1[i].Stepper = tmp;
                        PrChan1[i].Retrig = true;
                    }
                    tmp = patt.Stereo[line];
                    if (tmp >= 0) {
                        PrChan1[i].Stereo = (byte)tmp;
                    }
                    tmp = patt.Volume[line];
                    if (tmp >= 0) {
                        PrChan1[i].Volume = (byte)tmp;
                    }
                    tmp = patt.Width[line];
                    if (tmp >= 0) {
                        PrChan1[i].Width = (byte)tmp;
                    }
                    tmp = patt.Instrument[line];
                    if (tmp >= 0) {
                        PrChan1[i].Instrument = data.InsType1[tmp];
                    }
                    tmp = patt.Effect[line];
                    if (tmp >= 0) {
                        PrChan1[i].Effect = (char)tmp;
                    }
                    tmp = patt.FX1[line];
                    if (tmp >= 0) {
                        PrChan1[i].FX1 = (byte)tmp;
                    }
                    tmp = patt.FX2[line];
                    if (tmp >= 0) {
                        PrChan1[i].FX2 = (byte)tmp;
                    }
                } catch (Exception e) {
                    System.err.println("Error processing channel " + i + " because: " + e.toString());
                }
            }
            
            for (int i=0; i<PrChan1.length; ++i) {
                PrChan1[i].step();
                Channels1[i].copy(PrChan1[i]); //copy private to public
            }
            
            if (++counter >= data.Track[track].NoteUpdatePattern[counts]) {
                if (++line >= data.Track[track].PatternLength) {
                    line = 0;
                    if (++frame >= data.Track[track].Length) {
                        frame = data.Track[track].Loop;
                    }
                }
                counter = 0;
            }
        }
        else {
            //report nothing
            for (ChannelModel1 m : Channels1) {
                m.Stereo = 0; //mute
            }
            if (playing == 1) {
                playmode = 0;
                frame = 0;
                line = 0;
                counter = 0;
                counts = 0;
                for (ChannelModel1 m : PrChan1) {
                    m = new ChannelModel1();
                }
            }
        }
    }
    
    public int parseNote(String n) {
        if (n == null) return -1;
        String note = n.replace(':', ' ').trim();
        if (n.equalsIgnoreCase("xxxxxx")) {
            return 0;
        }
        if (note.length() == 6) {
            int tmp = Integer.parseInt(note, 16);
            if (tmp > 0) return tmp;
        }
        if (note.length() == 3) {
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
