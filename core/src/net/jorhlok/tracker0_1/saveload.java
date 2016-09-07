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
    
    int Width;
    int Height;
    int xCursor;
    int yCursor;
    
    String PathLoad = "~/Documents/jtp/a.jtp";
    String PathSave = "~/Documents/jtp/a.jtp";
    
    public saveload() {
        super();
        xCursor = 0;
        yCursor = 0;
    }

    @Override
    public void update() {
        
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
