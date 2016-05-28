package net.jorhlok.tracker0_1;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Array;

/**
 * This tracker is split into full screen pages.
 * @author jorh
 */
public abstract class page {
    public Array<Byte> Event;
            
    public page() {
        Event = new Array<Byte>();
    }
    
    public void input(byte e) {
        Event.add(e);
    }
    
    public void update() {
        
    }
    
    public abstract void draw(Batch batch, BitmapFont font, float deltaTime);
}
