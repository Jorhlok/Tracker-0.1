package net.jorhlok.tracker0_1;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.Array;

/**
 * Event listener for the main process
 * @author jorh
 */
public class inputty extends InputAdapter {
    public Array<Byte> Event; // 32 to 126 is that char, negative is some kind of miscellaneous event
    private boolean Shift;
    private boolean Control;
    
    public enum Events {
        BACKSPACE(-1),
        HOME(-2),
        END(-3),
        ESCAPE(-4),
        INSERT(-5),
        PAGE_UP(-6),
        PAGE_DOWN(-7),
        TAB(-8),
        SHIFT_TAB(-9),
        CTRL_TAB(-10),
        UP(-11),
        DOWN(-12),
        LEFT(-13),
        RIGHT(-14),
        CTRL_S(-15);
        
        
        byte val;
        
        Events(int b) {
            this.val = (byte)b;
        }
    }
    
    public inputty() {
        Event = new Array<Byte>();
    }
    
    @Override
    public boolean keyDown(int k) {
        switch (k) {
            case Input.Keys.BACKSPACE:
                Event.add(Events.BACKSPACE.val);
                break;
            case Input.Keys.CONTROL_LEFT:
            case Input.Keys.CONTROL_RIGHT:
                Control = true;
                break;
            case Input.Keys.END:
                Event.add(Events.END.val);
                break;
            case Input.Keys.HOME:
                Event.add(Events.HOME.val);
                break;
            case Input.Keys.ESCAPE:
                Event.add(Events.ESCAPE.val);
                break;
            case Input.Keys.INSERT:
                Event.add(Events.INSERT.val);
                break;
            case Input.Keys.PAGE_UP:
                Event.add(Events.PAGE_UP.val);
                break;
            case Input.Keys.PAGE_DOWN:
                Event.add(Events.PAGE_DOWN.val);
                break;
            case Input.Keys.SHIFT_LEFT:
            case Input.Keys.SHIFT_RIGHT:
                Shift = true;
                break;
            case Input.Keys.TAB:
                if (Shift && !Control) Event.add(Events.SHIFT_TAB.val);
                else if (Control && !Shift) Event.add(Events.CTRL_TAB.val);
                else if (!Shift && !Control) Event.add(Events.TAB.val);
                break;
            case Input.Keys.UP:
                Event.add(Events.UP.val);
                break;
            case Input.Keys.DOWN:
                Event.add(Events.DOWN.val);
                break;
            case Input.Keys.LEFT:
                Event.add(Events.LEFT.val);
                break;
            case Input.Keys.RIGHT:
                Event.add(Events.RIGHT.val);
                break;
            case Input.Keys.S:
                if (Control) Event.add(Events.CTRL_S.val);
                break;
        }
        return true;
    }
    
    @Override
    public boolean keyUp(int k) {
        switch (k) {
            case Input.Keys.CONTROL_LEFT:
            case Input.Keys.CONTROL_RIGHT:
                Control = false;
                break;
            case Input.Keys.SHIFT_LEFT:
            case Input.Keys.SHIFT_RIGHT:
                Shift = false;
                break;
        }
        return true;
    }
    
    @Override
    public boolean keyTyped(char c) {
        if (c >= 32 && c <= 126) Event.add((byte)c); // printable ASCII characters
        return true;
    }
}
