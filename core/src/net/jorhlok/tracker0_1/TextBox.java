package net.jorhlok.tracker0_1;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 *
 * @author jorh
 */
public class TextBox {
    public String Text;
    
    public TextBox() {
        
    }
    
    public TextBox(String t) {
        Text = t;
    }
    
    public int length() {
        return Text.length();
    }
    
    public void draw(Batch batch, BitmapFont font, float deltaTime, Color col, Color flc, int xoff, int yoff, int xc, boolean insert) {
        if (xc >= 0 && xc <= Text.length()) {
            font.setColor(flc);
            if (insert)
                font.draw(batch, "|", xoff+xc*8-4, yoff);
            else
                font.draw(batch, "█", xoff+xc*8, yoff);
        }
        font.setColor(col);
        font.draw(batch, Text, xoff, yoff);
    }
    
    public void delete(int xc, boolean insert) {
        if (Text.length() > 0) {
            if (insert) --xc;
            if (xc == Text.length()-2)
                Text = Text.substring(0, xc+1);
            else if (xc < Text.length()-1)
                Text = Text.substring(0,xc+1) + Text.substring(xc+2);
        }
    }
    
    public void backspace(int xc) {
        if (Text.length() > 0) {
            if (xc == 1)
                Text = Text.substring(xc--);
            else if (xc == Text.length())
                Text = Text.substring(0, --xc);
            else if (xc != 0)
                Text = Text.substring(0,xc-1) + Text.substring(xc--);
        }
    }
    
    public void type(char c, int xc, boolean insert) {
        if (xc == Text.length())
            Text += c;
        else if (insert)
            Text = Text.substring(0, xc) + c + Text.substring(xc);
        else 
            Text = Text.substring(0, xc) + c + Text.substring(xc+1);
    }
}
