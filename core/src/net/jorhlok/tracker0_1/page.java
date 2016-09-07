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
    
    public page() {
        Event = new Array<Byte>();
    }
    
    public void input(byte e) {
        Event.add(e);
    }
    
    public abstract void update();
    
    public abstract void draw(Batch batch, BitmapFont font, float deltaTime);
}
