package net.jorhlok.tracker0_1;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import net.jorhlok.tracker0_1.jorhtrackpack.jtpfile;

/**
 * Saves and loads jtp files
 * First version - two editable strings for load and save, hit enter to do the operation.
 * @author jorh
 */
public class saveload extends page {
    jtpfile jtp;
    
    int xCursor;
    int yCursor;
    boolean insert;
    
    TextBox PLoad = new TextBox("~/Documents/jtp/a.jtp");
    TextBox PSave = new TextBox("~/Documents/jtp/a.jtp");
    
//    String PathLoad = "~/Documents/jtp/a.jtp";
//    String PathSave = "~/Documents/jtp/a.jtp";
    
    public saveload() {
        super();
        xCursor = 0;
        yCursor = 0;
        insert = true;
    }

    @Override
    public void update() {
        while (Event.size > 0) {
            byte b = Event.pop();
            
            if (b >= 32 && b <= 126) {
                if (yCursor == 0)
                    PLoad.type((char)b, xCursor++, insert);
                else if (yCursor == 2)
                    PSave.type((char)b, xCursor++, insert);
            }
            else switch (b) {
                case -5: //insert
                    insert = !insert;
                    break;
                case -11: //up
                    if (yCursor > 0) --yCursor;
                    break;
                case -12: //down
                    if (yCursor < 3) ++yCursor;
                    break;
                case -13: //left
                    if ((yCursor & 1) == 0) --xCursor;
                    break;
                case -14: //right
                    if ((yCursor & 1) == 0) ++xCursor;
                    break;
                case -1: //backspace
                    if (yCursor == 0)
                        PLoad.backspace(xCursor--);
                    else if (yCursor == 2)
                        PSave.backspace(xCursor--);
                    break;
                case -16: //delete
                    if (yCursor == 0)
                        PLoad.delete(xCursor, insert);
                    else if (yCursor == 2)
                        PSave.delete(xCursor, insert);
                    break;
                case -17: //enter
                    if (yCursor == 1) {
                        //load
                    }
                    else if (yCursor == 3) {
                        //save
                    }
                    break;
            }
            if (xCursor < 0)
                xCursor = 0;
            if (yCursor == 0 && xCursor > PLoad.length()) 
                xCursor = PLoad.length();
            else if (yCursor == 2 && xCursor > PSave.length())
                xCursor = PSave.length();
        }
    }

    @Override
    public void draw(Batch batch, BitmapFont font, float deltaTime) {
        PLoad.draw(batch, font, deltaTime, fg3, flc, 8, 464-14*8, (yCursor == 0)?xCursor:-1, insert);
        drawButton(batch, font, deltaTime, " Load ", fg4, (yCursor == 1)?flc:bgn, 8, 14*9);
        PSave.draw(batch, font, deltaTime, fg1, flc, 8, 464-14*11, (yCursor == 2)?xCursor:-1, insert);
        drawButton(batch, font, deltaTime, " Save ", fg2, (yCursor == 3)?flc:bgn, 8, 14*12);
    }
    
    private void drawButton(Batch batch, BitmapFont font, float deltaTime, String button, Color col, Color back, int xoff, int yoff) {
        font.setColor(back);
        String str = "";
        for (int i=0; i<button.length(); ++i)
            str += "█";
        font.draw(batch, str, xoff, 464-yoff);
        font.setColor(col);
        font.draw(batch, button, xoff, 464-yoff);
    }
}
