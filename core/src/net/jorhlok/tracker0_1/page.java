package net.jorhlok.tracker0_1;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Array;

/**
 * This tracker is split into full screen pages.
 * @author jorh
 */
public abstract class page {
    public Array<Byte> Event;
            
    Color fg1;
    Color fg2;
    Color fg3;
    Color fg4;
    Color flc;
    Color bgc;
    Color bgn;
    
    public page() {
        Event = new Array<>();
    }
    
    public void input(byte e) {
        Event.add(e);
    }
    
    public abstract void update();
    
    public abstract void draw(Batch batch, BitmapFont font, float deltaTime);
    
    protected void drawButton(Batch batch, BitmapFont font, float deltaTime, String button, Color col, Color back, int xoff, int yoff) {
        font.setColor(back);
        String str = "";
        for (int i=0; i<button.length(); ++i)
            str += "█";
        font.draw(batch, str, xoff, 464-yoff);
        font.setColor(col);
        font.draw(batch, button, xoff, 464-yoff);
    }
}
