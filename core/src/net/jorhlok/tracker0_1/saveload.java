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
    float FileFail = 0;
    float FailureTime = 4;
    float FileSuccess = 0;
    float SuccessTime = 4;
    
    TextBox PathLoad = new TextBox("~/Documents/jtp/a.jtp");
    TextBox PathSave = new TextBox("~/Documents/jtp/a.jtp");
    
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
                    PathLoad.type((char)b, xCursor++, insert);
                else if (yCursor == 2)
                    PathSave.type((char)b, xCursor++, insert);
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
                        PathLoad.backspace(xCursor--);
                    else if (yCursor == 2)
                        PathSave.backspace(xCursor--);
                    break;
                case -16: //delete
                    if (yCursor == 0)
                        PathLoad.delete(xCursor, insert);
                    else if (yCursor == 2)
                        PathSave.delete(xCursor, insert);
                    break;
                case -17: //enter
                    boolean success = true;
                    if (yCursor == 1) {
                        //load
                        jtp.Path = PathLoad.Text;
                        success = jtp.load();
                    }
                    else if (yCursor == 3) {
                        //save
                        jtp.Path = PathSave.Text;
                        success = jtp.save();
                    }
                    if (!success) {
                        FileFail = FailureTime;
                        FileSuccess = 0;
                    }
                    else {
                        FileSuccess = SuccessTime;
                        FileFail = 0;
                    }
                    break;
            }
            if (xCursor < 0)
                xCursor = 0;
            if (yCursor == 0 && xCursor > PathLoad.length()) 
                xCursor = PathLoad.length();
            else if (yCursor == 2 && xCursor > PathSave.length())
                xCursor = PathSave.length();
        }
    }

    @Override
    public void draw(Batch batch, BitmapFont font, float deltaTime) {
        PathLoad.draw(batch, font, deltaTime, fg3, flc, 8, 464-14*8, (yCursor == 0)?xCursor:-1, insert);
        drawButton(batch, font, deltaTime, " Load ", fg4, (yCursor == 1)?flc:bgn, 8, 14*9);
        PathSave.draw(batch, font, deltaTime, fg1, flc, 8, 464-14*11, (yCursor == 2)?xCursor:-1, insert);
        drawButton(batch, font, deltaTime, " Save ", fg2, (yCursor == 3)?flc:bgn, 8, 14*12);
        if (FileFail > 0) {
            FileFail -= deltaTime;
            drawButton(batch, font, deltaTime, " FILE OPERATION FAILURE ", new Color(1f,0f,0f,1f), flc, 8, 0);
        }
        else if (FileSuccess > 0) {
            FileSuccess -= deltaTime;
            drawButton(batch, font, deltaTime, " FILE OPERATION SUCCESS ", new Color(0f,0f,0f,1f), bgn, 8, 0);
        }
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
