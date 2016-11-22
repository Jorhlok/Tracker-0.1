package net.jorhlok.tracker0_1;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import net.jorhlok.tracker0_1.playroutine.AsyncPlay;

/**
 * Play controls for the tracker
 * First version - only Track[0] plays arbitrary frames in order a frame selector and maybe scope graphics
 * @author Jorhlok
 */
public class playtrack extends page {
    
    int xCursor = 0; //0 is play, 1 is pause, 2 is stop, 3+ is FrameSelect
    TextBox FrameSelect = new TextBox("00");
    public AsyncPlay aplay;
    char ScopeChar = '.';

    @Override
    public void update() {
        while (Event.size > 0) {
            byte b = Event.pop();
            
            if (b > 32 && b <= 126) {
                if (xCursor > 2)
                    FrameSelect.type((char)b, (xCursor++)-3, false);
            }
            else switch (b) {
                case 32: //space
                    if (ScopeChar == '.') ScopeChar = '|';
                    else ScopeChar = '.';
                    break;
                case -5: //insert
                    break;
                case -11: //up
                    break;
                case -12: //down
                    break;
                case -13: //left
                    --xCursor;
                    break;
                case -14: //right
                    ++xCursor;
                    break;
                case -1: //backspace
                    break;
                case -16: //delete
                    break;
                case -17: //enter
                    switch (xCursor) {
                        case 0:
                            //play
                            aplay.pr.playmode = 2;
                            break;
                        case 1:
                            //pause
                            aplay.pr.playmode = 0;
                            break;
                        case 2:
                            //stop
                            aplay.pr.playmode = 1;
                            break;
                        default:
                            //change frame
                            try {
                                int index = Integer.parseInt(FrameSelect.Text, 16);
                                aplay.pr.frame = index;
                                aplay.pr.line = aplay.pr.counter = 0;
                                //TODO : add thread safety
                            } catch (Exception e) {
                                //nothing
                            }
                    }
                    break;
            }
            if (xCursor < 0)
                xCursor = 0;
            if (xCursor > 4)
                xCursor = 3;
        }
    }

    @Override
    public void draw(Batch batch, BitmapFont font, float deltaTime) {
        drawButton(batch, font, deltaTime, " PLAY ", fg1, (xCursor == 0)?flc:bgn, 8, 7);
        drawButton(batch, font, deltaTime, " PAUSE ", fg2, (xCursor == 1)?flc:bgn, 8*8, 7);
        drawButton(batch, font, deltaTime, " STOP ", fg4, (xCursor == 2)?flc:bgn, 8*16, 7);
        FrameSelect.draw(batch, font, deltaTime, fg3, flc, 8*23, 464-7, xCursor-3, false);
        drawButton(batch, font, deltaTime, " " + Integer.toHexString(aplay.pr.frame) 
                + " Frame " + Integer.toHexString(aplay.pr.data.Track[aplay.pr.track].Sequence[aplay.pr.frame]) 
                + " " + aplay.pr.data.Track[aplay.pr.track].Frame[aplay.pr.data.Track[aplay.pr.track].Sequence[aplay.pr.frame]].toString() 
                + " Line " + Integer.toHexString(aplay.pr.line) + " ", fg2, bgn, 8, 14*2);
        
        short[] buf = aplay.dispBuf.clone();
        float[] left = new float[buf.length/2];
        float[] right = new float[buf.length/2];
        for (int i=0; i<buf.length; ++i) {
            float[] side = ((i&1)==0)?left:right;
            side[i/2] = buf[i]*(float)192/(1<<15)+232;
        }
        for (int i=0; i<left.length; ++i) {
            font.setColor(fg4);
            font.draw(batch, ""+ScopeChar, (i-2)*(float)1000/left.length, left[i]);
            font.setColor(fg1);
            font.draw(batch, ""+ScopeChar, (i-2)*(float)1000/left.length, right[i]);
        }
    }
    
}
