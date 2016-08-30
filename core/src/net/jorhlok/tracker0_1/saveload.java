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
    
    Color fg1;
    Color fg2;
    Color fg3;
    Color fg4;
    Color flc;
    Color bgc;
    
    String PathLoad;
    String PathSave;

    @Override
    public void draw(Batch batch, BitmapFont font, float deltaTime) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
