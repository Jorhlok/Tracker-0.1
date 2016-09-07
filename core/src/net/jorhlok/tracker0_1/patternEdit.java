package net.jorhlok.tracker0_1;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import net.jorhlok.tracker0_1.jorhtrackpack.insPattern;
import net.jorhlok.tracker0_1.jorhtrackpack.jtpfile;
import net.jorhlok.tracker0_1.jorhtrackpack.pcmPattern;

/**
 * Pattern edit page. This is where most of the music making will go on.
 * @author jorh
 */
public class patternEdit extends page {
    jtpfile jtp;
    short Track;
    short Frame;
    
    int Width;
    int Height;
    int xCursor;
    int yCursor;
    
    public patternEdit() {
        super();
        
        Track = -1;
        Frame = -1;
        
        Width = 122;
        Height = 32;
        xCursor = 0;
        yCursor = 0;
    }

    @Override
    public void update () {
        //run through events
        while (Event.size > 0) {
            byte b = Event.pop();
            
            if (b >= 32 && b <= 126) {
                String str;
                //typeable characters
                if (xCursor >= 0 && xCursor < 8*14) {
                    insPattern p = jtp.Track[Track].InsPattern[ jtp.Track[Track].Frame[Frame].InsPattern[xCursor / 14] ];
                    //modify pattern
                    int col = xCursor % 14;
                    if (col < 6) { //note
                        if (p.Note[yCursor] == null) p.Note[yCursor] = "      ";
                        char[] ca = p.Note[yCursor].toCharArray();
                        ca[col] = (char)b;
                        str = "";
                        for (int i=0; i<ca.length && i<6; ++i) {
                            str += ca[i];
                        }
                        p.Note[yCursor] = str;
                    }
                    
                    switch (col) {
                        case 6: //stereo
                            if (b >= '0' && b <= '3') {
                                p.Stereo[yCursor] = (byte)(b-'0');
                            }
                            else if (b == ' ') p.Stereo[yCursor] = -1;
                            break;
                        case 7: //volume
                            if (b >= '0' && b <= '9') {
                                p.Volume[yCursor] = (byte)(b-'0');
                            }
                            else if (b >= 'a' && b <= 'f') {
                                p.Volume[yCursor] = (byte)(b-'a'+10);
                            }
                            else if (b >= 'A' && b <= 'F') {
                                p.Volume[yCursor] = (byte)(b-'A'+10);
                            }
                            else if (b == ' ') p.Volume[yCursor] = -1;
                            break;
                        case 8: //width
                            if (b >= '0' && b <= '9') {
                                p.Width[yCursor] = (byte)(b-'0');
                            }
                            else if (b >= 'a' && b <= 'f') {
                                p.Width[yCursor] = (byte)(b-'a'+10);
                            }
                            else if (b >= 'A' && b <= 'F') {
                                p.Width[yCursor] = (byte)(b-'A'+10);
                            }
                            else if (b == ' ') p.Width[yCursor] = -1;
                            break;
                        case 9: //instrument high nybble
                            if (b >= '0' && b <= '9') {
                                if (p.Instrument[yCursor] < 0 || p.Instrument[yCursor] > 255) p.Instrument[yCursor] = 0;
                                p.Instrument[yCursor] = (short)( (b-'0')*16 + p.Instrument[yCursor]%16);
                            }
                            else if (b >= 'a' && b <= 'f') {
                                if (p.Instrument[yCursor] < 0 || p.Instrument[yCursor] > 255) p.Instrument[yCursor] = 0;
                                p.Instrument[yCursor] = (short)( (b-'a'+10)*16 + p.Instrument[yCursor]%16);
                            }
                            else if (b >= 'A' && b <= 'F') {
                                if (p.Instrument[yCursor] < 0 || p.Instrument[yCursor] > 255) p.Instrument[yCursor] = 0;
                                p.Instrument[yCursor] = (short)( (b-'A'+10)*16 + p.Instrument[yCursor]%16);
                            }
                            else if (b == ' ') p.Instrument[yCursor] = -1;
                            break;
                        case 10: //instrument low nybble
                            if (b >= '0' && b <= '9') {
                                if (p.Instrument[yCursor] < 0 || p.Instrument[yCursor] > 255) p.Instrument[yCursor] = 0;
                                p.Instrument[yCursor] = (short)( (b-'0') + (p.Instrument[yCursor]&0xF0) );
                            }
                            else if (b >= 'a' && b <= 'f') {
                                if (p.Instrument[yCursor] < 0 || p.Instrument[yCursor] > 255) p.Instrument[yCursor] = 0;
                                p.Instrument[yCursor] = (short)( (b-'a'+10) + (p.Instrument[yCursor]&0xF0) );
                            }
                            else if (b >= 'A' && b <= 'F') {
                                if (p.Instrument[yCursor] < 0 || p.Instrument[yCursor] > 255) p.Instrument[yCursor] = 0;
                                p.Instrument[yCursor] = (short)( (b-'A'+10) + (p.Instrument[yCursor]&0xF0) );
                            }
                            else if (b == ' ') p.Instrument[yCursor] = -1;
                            break;
                        case 11: //FX select
                            p.Effect[yCursor] = (char)b;
                            break;
                        case 12: //FX1 value
                            if (b >= '0' && b <= '9') {
                                p.FX1[yCursor] = (byte)(b-'0');
                            }
                            else if (b >= 'a' && b <= 'f') {
                                p.FX1[yCursor] = (byte)(b-'a'+10);
                            }
                            else if (b >= 'A' && b <= 'F') {
                                p.FX1[yCursor] = (byte)(b-'A'+10);
                            }
                            else if (b == ' ') p.FX1[yCursor] = -1;
                            break;
                        case 13: //FX2 value
                            if (b >= '0' && b <= '9') {
                                p.FX2[yCursor] = (byte)(b-'0');
                            }
                            else if (b >= 'a' && b <= 'f') {
                                p.FX2[yCursor] = (byte)(b-'a'+10);
                            }
                            else if (b >= 'A' && b <= 'F') {
                                p.FX2[yCursor] = (byte)(b-'A'+10);
                            }
                            else if (b == ' ') p.FX2[yCursor] = -1;
                            break;
                        default:
                    }
                }
                else {
                    pcmPattern p = jtp.Track[Track].PCMPattern[ jtp.Track[Track].Frame[Frame].PCMPattern ];
                    //modify pattern
                    int col = (xCursor - 8*14);
                    if (col < 6) {
                        if (p.Note[yCursor] == null) p.Note[yCursor] = "      ";
                        char[] ca = p.Note[yCursor].toCharArray();
                        ca[col] = (char)b;
                        str = "";
                        for (int i=0; i<ca.length && i<6; ++i) {
                            str += ca[i];
                        }
                        p.Note[yCursor] = str;
                    }
                    
                    switch (col) {
                        case 6: //stereo
                            if (b >= '0' && b <= '3') {
                                p.Stereo[yCursor] = (byte)(b-'0');
                            }
                            else if (b == ' ') p.Stereo[yCursor] = -1;
                            break;
                        case 7: //volume
                            if (b >= '0' && b <= '7') {
                                p.Volume[yCursor] = (byte)(b-'0');
                            }
                            else if (b == ' ') p.Volume[yCursor] = -1;
                            break;
                        case 8: //waveform high nybble
                            if (b >= '0' && b <= '9') {
                                if (p.Waveform[yCursor] < 0 || p.Waveform[yCursor] > 255) p.Waveform[yCursor] = 0;
                                p.Waveform[yCursor] = (short)( (b-'0')*16 + p.Waveform[yCursor]%16);
                            }
                            else if (b >= 'a' && b <= 'f') {
                                if (p.Waveform[yCursor] < 0 || p.Waveform[yCursor] > 255) p.Waveform[yCursor] = 0;
                                p.Waveform[yCursor] = (short)( (b-'a'+10)*16 + p.Waveform[yCursor]%16);
                            }
                            else if (b >= 'A' && b <= 'F') {
                                if (p.Waveform[yCursor] < 0 || p.Waveform[yCursor] > 255) p.Waveform[yCursor] = 0;
                                p.Waveform[yCursor] = (short)( (b-'A'+10)*16 + p.Waveform[yCursor]%16);
                            }
                            else if (b == ' ') p.Waveform[yCursor] = -1;
                            break;
                        case 9: //waveform low nybble
                            if (b >= '0' && b <= '9') {
                                if (p.Waveform[yCursor] < 0 || p.Waveform[yCursor] > 255) p.Waveform[yCursor] = 0;
                                p.Waveform[yCursor] = (short)( (b-'0') + (p.Waveform[yCursor]&0xF0) );
                            }
                            else if (b >= 'a' && b <= 'f') {
                                if (p.Waveform[yCursor] < 0 || p.Waveform[yCursor] > 255) p.Waveform[yCursor] = 0;
                                p.Waveform[yCursor] = (short)( (b-'a'+10) + (p.Waveform[yCursor]&0xF0) );
                            }
                            else if (b >= 'A' && b <= 'F') {
                                if (p.Waveform[yCursor] < 0 || p.Waveform[yCursor] > 255) p.Waveform[yCursor] = 0;
                                p.Waveform[yCursor] = (short)( (b-'A'+10) + (p.Waveform[yCursor]&0xF0) );
                            }
                            else if (b == ' ') p.Waveform[yCursor] = -1;
                            break;
                        default:
                    }
                }
                if (++xCursor == Width) {
                    xCursor = 0;
                    ++yCursor;
                    yCursor %= Height;
                }
            }
            else  switch (b) {
                case -8: //TAB - go to next pattern
                    if (xCursor > 8*14-1) xCursor %= 14;
                    else xCursor += 14;
                    if (xCursor >= Width) xCursor = Width-1; // edge case of tabbing from column 11-14 on pattern 8 to pattern 9
                    break;
                case -11: //UP
                    if (--yCursor < 0) yCursor = Height-1;
                    break;
                case -12: //DOWN
                    if (++yCursor > Height-1) yCursor = 0;
                    break;
                case -13: //LEFT
                    if (--xCursor < 0) xCursor = Width-1;
                    break;
                case -14: //RIGHT
                    if (++xCursor > Width-1) xCursor = 0;
                    break;
            }
        }
    }
    
    @Override
    public void draw(Batch batch, BitmapFont font, float deltaTime) {
        insPattern patt;
        for (int i=0; i<8; i++) {
            //check pattern
            patt = null;
            if (jtp.Track[Track] != null
                && jtp.Track[Track].Frame[Frame] != null
                && jtp.Track[Track].Frame[Frame].InsPattern[i] >= 0 ) {
                    patt = jtp.Track[Track].InsPattern[ jtp.Track[Track].Frame[Frame].InsPattern[i] ];
            }
            if (patt != null) {
                //draw pattern
                if (xCursor >= i*14 && xCursor < (i+1)*14) {
                    drawInsPatt(batch,font,deltaTime,patt,i%2,4,14+(i*14*8),xCursor-i*14,yCursor);
                }
                else {
                    drawInsPatt(batch,font,deltaTime,patt,i%2,4,14+(i*14*8));
                }
            }
        }
        
        pcmPattern pcmpatt = null;
        if (jtp.Track[Track] != null
            && jtp.Track[Track].Frame[Frame] != null
            && jtp.Track[Track].Frame[Frame].PCMPattern >= 0 
            && (pcmpatt = jtp.Track[Track].PCMPattern[ jtp.Track[Track].Frame[Frame].PCMPattern ]) != null ) {
                drawPCMPatt(batch,font,deltaTime,pcmpatt,0,4,14+(8*14*8),xCursor-8*14,yCursor);
        }
        
    }
    
    private void drawInsPatt(Batch batch, BitmapFont font, float deltaTime, insPattern patt, int col, int hilite, int xoff) {
        drawInsPatt(batch,font,deltaTime,patt,col,hilite,xoff,-1,-1); // cursor not present
    }

    private void drawInsPatt(Batch batch, BitmapFont font, float deltaTime, insPattern patt, int col, int hilite, int xoff, int xc, int yc) {
        for (short i=0; i<patt.Length; i++) {
            Color rowcol;
            if (i%hilite == 0) {
                if (col == 0) rowcol = fg2;
                else /*if (col == 1)*/ rowcol = fg4;
            }
            else {
                if (col == 0) rowcol = fg1;
                else /*if (col == 1)*/ rowcol = fg3;
            }
            String row = patt.noteToString(i);
            
            //readability adjustments
            if (row.startsWith("::::::")) row = "▄▄▄▄▄▄" + row.substring(6);
            if (row.endsWith(" //")) row = row.substring(0, 11) + "■■■";
            if (row.endsWith("//")) row = row.substring(0, 12) + "■■";
            
            //draw row
            font.setColor(rowcol);
            font.draw(batch, row, xoff, 464-i*14);
            if (i == yc && xc >= 0 && xc <= 14) {
                //draw character under cursor on top
                font.setColor(flc);
                font.draw(batch, row.substring(xc, xc+1).replace(' ', '▄'), xoff+xc*8, 464-i*14);
            }
        }
    }
    
    private void drawPCMPatt(Batch batch, BitmapFont font, float deltaTime, pcmPattern patt, int col, int hilite, int xoff, int xc, int yc) {
        for (short i=0; i<patt.Length; i++) {
            Color rowcol;
            if (i%hilite == 0) {
                if (col == 0) rowcol = fg2;
                else /*if (col == 1)*/ rowcol = fg4;
            }
            else {
                if (col == 0) rowcol = fg1;
                else /*if (col == 1)*/ rowcol = fg3;
            }
            String row = patt.noteToString(i);
            
            //readability adjustments
            if (row.startsWith("::::::")) row = "▄▄▄▄▄▄" + row.substring(6);
            
            //draw row
            font.setColor(rowcol);
            font.draw(batch, row, xoff, 464-i*14);
            if (i == yc && xc >= 0 && xc <= 10) {
                //draw character under cursor on top
                font.setColor(flc);
                font.draw(batch, row.substring(xc, xc+1).replace(' ', '▄'), xoff+xc*8, 464-i*14);
            }
        }
    }
}
