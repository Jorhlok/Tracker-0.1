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
    
    String PathLoad = "~/Documents/jtp/a.jtp";
    String PathSave = "~/Documents/jtp/a.jtp";
    
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
                if (yCursor == 0) {
                    
                }
                else if (yCursor == 2) {
                    
                }
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
            }
            
            if (yCursor == 0 && xCursor > PathLoad.length()) 
                xCursor = PathLoad.length();
            else if (yCursor == 2 && xCursor > PathSave.length())
                xCursor = PathLoad.length();
        }
    }

    @Override
    public void draw(Batch batch, BitmapFont font, float deltaTime) {
        drawPath(batch, font, deltaTime, PathLoad, fg3, 8, 14*8, (yCursor == 0)?xCursor:-1);
        drawButton(batch, font, deltaTime, "Load", fg4, 8, 14*9, yCursor == 1);
        drawPath(batch, font, deltaTime, PathSave, fg1, 8, 14*11, (yCursor == 2)?xCursor:-1);
        drawButton(batch, font, deltaTime, "Save", fg2, 8, 14*12, yCursor == 3);
    }
    
    private void drawPath(Batch batch, BitmapFont font, float deltaTime, String path, Color col, int xoff, int yoff, int xc) {
        font.setColor(col);
        font.draw(batch, path, xoff, 464-yoff);
        if (xc >= 0 && xc < path.length()) {
            font.setColor(flc);
            font.draw(batch, path.substring(xc, xc+1).replace(' ', '▄'), xoff+xc*8, 464-yoff);
        }
    }
    
    private void drawButton(Batch batch, BitmapFont font, float deltaTime, String button, Color col, int xoff, int yoff, boolean hilite) {
        if (hilite) font.setColor(flc);
        else font.setColor(col);
        font.draw(batch, button, xoff, 464-yoff);
    }
}
