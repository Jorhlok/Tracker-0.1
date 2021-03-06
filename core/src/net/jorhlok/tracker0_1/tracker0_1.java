package net.jorhlok.tracker0_1;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import net.jorhlok.tracker0_1.jorhtrackpack.jtpfile;
import net.jorhlok.tracker0_1.jorhtrackpack.jts;
import net.jorhlok.tracker0_1.playroutine.AudioInterface;
import net.jorhlok.tracker0_1.playroutine.AsyncAudioDevice;
import net.jorhlok.tracker0_1.playroutine.AsyncPlay;
import net.jorhlok.tracker0_1.playroutine.PlayRoutine;


/**
 * tracker-0.1
 * @author jorh
 * 
 * Meant to be a just barely functional tracker.
 * Ugly and lacking major usability features.
 * Meant for JPSG160XY series of (imaginary) sound chips.
 * Y = M(ono) or S(tereo)
 * X = number of voices (an odd number indicates the extra PCM channel)
 * JPSG1604, 05, 08, 09
 */
public class tracker0_1 extends ApplicationAdapter {
    String AppVersion = "0.1.0";
    
    inputty Inputty;
    SpriteBatch batch;
    BitmapFont font;
    Color bg1;
    Color bg2;
    Color bgc;
    Color bgn;
    Color fg1;
    Color fg2;
    Color fg3;
    Color fg4;
    Color fl1;
    Color fl2;
    Color flc;
    float strobe;
    byte strobespeed; // 0-20 twentieths of a second
    
    jtpfile jtp;
    page CurrentPage;
    patternEdit PEdit;
    saveload SL;
    playtrack PT;
    
    AsyncPlay aplay;
    AudioInterface audio;
    
    public tracker0_1() {
        super();
    }
    
    public tracker0_1(AudioInterface ai) {
        this();
        audio = ai;
    }

    @Override
    public void create () {
        Inputty = new inputty();
        Gdx.input.setInputProcessor(Inputty);
        batch = new SpriteBatch();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 14;
//        parameter.genMipMaps = true;
        parameter.magFilter = TextureFilter.Linear;
        parameter.minFilter = TextureFilter.Linear;
        parameter.characters = parameter.characters + "█▄■";
        font = generator.generateFont(parameter);
        generator.dispose();
        
        bg1 = new Color(0f, 0f, 0.1f, 1f); // blackened blue
        bg2 = new Color(0.8f, 1f, 0.7f, 1f); // pale nature green
        fg1 = new Color(0.4f, 0.8f, 0.6f, 1f); // teal-green
        fg2 = new Color(0.3f, 0.6f, 0.9f, 1f); // cyan-blue
        fg3 = new Color(0.8f, 0.7f, 0.3f, 1f); // amber
        fg4 = new Color(1f, 0.5f, 0.3f, 1f); // red-orange
        fl1 = new Color(1f, 1f, 1f, 1f); // white
        fl2 = new Color(0f, 0f, 0f, 1f); // black
        bgc = new Color(bg1);
        bgn = new Color(bg2);
        flc = new Color(fl1);
        
        strobe = 0;
        strobespeed = 5;
//        String str = Integer.toString(strobespeed*5+1000);
//        str = str.substring(1, 2) + "." + str.substring(2);
//        System.out.println(str);

        jtp = new jtpfile();
        jtp.newTrack(0);
        jts track = jtp.Track[0];
        track.Length = 1;
        track.newInsPattern(0);
        track.newInsPattern(1);
        track.newInsPattern(2);
        track.newInsPattern(3);
        track.newInsPattern(4);
        track.newInsPattern(5);
        track.newInsPattern(6);
        track.newInsPattern(7);
        track.newPCMPattern(0);
        track.newFrame(0);
        track.Frame[0].InsPattern[0] = 0;
        track.Frame[0].InsPattern[1] = 1;
        track.Frame[0].InsPattern[2] = 2;
        track.Frame[0].InsPattern[3] = 3;
        track.Frame[0].InsPattern[4] = 4;
        track.Frame[0].InsPattern[5] = 5;
        track.Frame[0].InsPattern[6] = 6;
        track.Frame[0].InsPattern[7] = 7;
        track.Frame[0].PCMPattern = 0;
        track.Sequence[0] = 0;
        
        
        for (int i=0;i<16;++i) jtp.PCM4[i] = new byte[2];
        jtp.PCM4[0][0] = 0;
        jtp.PCM4[0][1] = 0;
        jtp.PCM4[1][0] = 0;
        jtp.PCM4[1][1] = -1;
        jtp.PCM4[2][0] = 1;
        jtp.PCM4[2][1] = -1;
        jtp.PCM4[3][0] = 1;
        jtp.PCM4[3][1] = -2;
        jtp.PCM4[4][0] = 2;
        jtp.PCM4[4][1] = -2;
        jtp.PCM4[5][0] = 2;
        jtp.PCM4[5][1] = -3;
        jtp.PCM4[6][0] = 3;
        jtp.PCM4[6][1] = -3;
        jtp.PCM4[7][0] = 3;
        jtp.PCM4[7][1] = -4;
        jtp.PCM4[8][0] = 4;
        jtp.PCM4[8][1] = -4;
        jtp.PCM4[9][0] = 4;
        jtp.PCM4[9][1] = -5;
        jtp.PCM4[10][0] = 5;
        jtp.PCM4[10][1] = -5;
        jtp.PCM4[11][0] = 5;
        jtp.PCM4[11][1] = -6;
        jtp.PCM4[12][0] = 6;
        jtp.PCM4[12][1] = -6;
        jtp.PCM4[13][0] = 6;
        jtp.PCM4[13][1] = -7;
        jtp.PCM4[14][0] = 7;
        jtp.PCM4[14][1] = -7;
        jtp.PCM4[15][0] = 7;
        jtp.PCM4[15][1] = -8;
        
        jtp.newInsType1(0);
        jtp.InsType1[0].PCM[0] = 0;
        jtp.InsType1[0].PCM[1] = 1;
        jtp.InsType1[0].PCM[2] = 2;
        jtp.InsType1[0].PCM[3] = 3;
        jtp.InsType1[0].PCM[4] = 4;
        jtp.InsType1[0].PCM[5] = 5;
        jtp.InsType1[0].PCM[6] = 6;
        jtp.InsType1[0].PCM[7] = 7;
        jtp.InsType1[0].PCM[8] = 8;
        jtp.InsType1[0].PCM[9] = 9;
        jtp.InsType1[0].PCM[10] = 10;
        jtp.InsType1[0].PCM[11] = 11;
        jtp.InsType1[0].PCM[12] = 12;
        jtp.InsType1[0].PCM[13] = 13;
        jtp.InsType1[0].PCM[14] = 14;
        jtp.InsType1[0].PCM[15] = 15;
        
        jtp.InsType1[0].PCMLength[0] = 2;
        jtp.InsType1[0].PCMLength[1] = 2;
        jtp.InsType1[0].PCMLength[2] = 2;
        jtp.InsType1[0].PCMLength[3] = 2;
        jtp.InsType1[0].PCMLength[4] = 2;
        jtp.InsType1[0].PCMLength[5] = 2;
        jtp.InsType1[0].PCMLength[6] = 2;
        jtp.InsType1[0].PCMLength[7] = 2;
        jtp.InsType1[0].PCMLength[8] = 2;
        jtp.InsType1[0].PCMLength[9] = 2;
        jtp.InsType1[0].PCMLength[10] = 2;
        jtp.InsType1[0].PCMLength[11] = 2;
        jtp.InsType1[0].PCMLength[12] = 2;
        jtp.InsType1[0].PCMLength[13] = 2;
        jtp.InsType1[0].PCMLength[14] = 2;
        jtp.InsType1[0].PCMLength[15] = 2;
        
        PEdit = new patternEdit();
        SL = new saveload();
        PT = new playtrack();
        
        CurrentPage = SL;
        
        PEdit.fg1 = SL.fg1 = PT.fg1 = fg1;
        PEdit.fg2 = SL.fg2 = PT.fg2 = fg2;
        PEdit.fg3 = SL.fg3 = PT.fg3 = fg3;
        PEdit.fg4 = SL.fg4 = PT.fg4 = fg4;
        PEdit.flc = SL.flc = PT.flc = flc;
        PEdit.bgc = SL.bgc = PT.bgc = bgc;
        PEdit.bgn = SL.bgn = PT.bgn = bgn;
        
        PEdit.jtp = SL.jtp = jtp;
        PEdit.Track = 0;
        PEdit.Frame = 0;
        
        audio = null; //override desktop specific audio code
        
        aplay = new AsyncPlay();
        aplay.pr = new PlayRoutine();
        aplay.pr.data = jtp;
        aplay.pr.track = 0;
        if (audio == null) {
            //GDX only implementation, you might stutter
            System.err.println("Using GDX only audio device, might have stuttering.");
            audio = new AsyncAudioDevice();
        }
        
        audio.setAPlay(aplay);
        audio.create();

        PT.aplay = aplay;
    }

    @Override
    public void render () {
        for (int i=0; i<Inputty.Event.size; ++i) {
            System.out.println(Inputty.Event.get(i).toString());
            if (Inputty.Event.get(i) == inputty.Events.CTRL_TAB.val) {
                //trap ctrl+tab
                //switch to sub-page
                if (CurrentPage instanceof patternEdit) {
                    CurrentPage = PT;
                }
                else if (CurrentPage instanceof playtrack) {
                    CurrentPage = PEdit;
                }
                else if (CurrentPage instanceof saveload) {
                    
                }
            }
            else if (Inputty.Event.get(i) == inputty.Events.SHIFT_TAB.val) {
                //trap shift+tab
                //switch to next page
                if (CurrentPage instanceof patternEdit) {
                    CurrentPage = SL;
                }
                else if (CurrentPage instanceof playtrack) {
                    
                }
                else if (CurrentPage instanceof saveload) {
                    CurrentPage = PEdit;
                }
            }
            else {
                CurrentPage.input(Inputty.Event.get(i));
            }
        }
        Inputty.Event.clear();
        
        
        strobe += Gdx.graphics.getDeltaTime();
        if (strobe > 0.05f*strobespeed) {
            strobe -= 0.05f*strobespeed;
            if(flc.equals(fl1)) flc.set(fl2);
            else flc.set(fl1);
        }
        
        PEdit.update();
        SL.update();
        PT.update();
        
        Gdx.gl.glClearColor(bgc.r,bgc.g,bgc.b,bgc.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        
        batch.begin();
        try {
        CurrentPage.draw(batch, font, Gdx.graphics.getDeltaTime());
        } catch (Exception e) {
            System.err.println("Some error drawing " + CurrentPage.getClass().getTypeName() + " because:\n" + e.toString());
        }
//        PEdit.draw(batch, font, Gdx.graphics.getDeltaTime());
//        SL.draw(batch, font, Gdx.graphics.getDeltaTime());

        batch.end();
    }
    
    @Override
    public void dispose() {
        System.err.println("Disposing");
        audio.dispose();
    }
}

/**
 * 0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF
 * 0123456789012345678901234567890123456789012345678901234567890123456789
 * ssssssSVWiiEXY (std channel) (stepper Stereo Volume pulseWidth instrument Effects XY)
 * 123456789ABCDE (14)
 * llllllSVww (pcm channel) (length/stepper Stereo Volume waveform)
 * 123456789A (10)
 * 122 wide
 * 32 tall
 */

/**
 * Track and File attributes editor > subpage File load/save
 * Waveform editor > subpage Waveform load/save
 * Instrument editor > subpage Instrument load/save
 * Pattern editor > subpage Track/Frame editor
 * Song viewer > subpage Scope viewer
 */

/**
 * Ctrl+Tab > swap page and subpage
 * Shift+Tab > next page
 * Alphanumeric + symbols > enter appropriate data
 * Ctrl+S > overwrite file if applicable
 */

/**
 * Track Attributes
 * ==================
 * Name
 * Author
 * Comment
 * 
 * Target chip
 * Pattern Length
 * Samplerate
 * Samples per update
 * Note update pattern
 * 
 * Additional Options
 * ==================
 * Background light/dark
 * Line highlight factor
 * Strobe speed
 * Save to wave file
 */

/**
 * Track file attributes (0-255) (XY.jts) (jorhlok track sequences)
 * =====================
 * Name
 * Author
 * Comment
 * 
 * Target chip
 * Pattern Length
 * Updates per Second
 * Notes per Second or Updates per Note
 * 
 * The below may not be necessary
 * ==============================
 * PCM4 mapping (0-4095) (XYZ.hp4) (hexadecimal pcm 4-bit)
 * PCM8 mapping (0-255) (XY.hp8) (hexadecimal pcm 8-bit)
 * Instrument mapping (0-255) (XY.ji0) or (XY.ji1) (jorhlok tracker instrument)
 */

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
