package net.jorhlok.tracker0_1.playroutine;

/**
 * This allows platform specific audio interface 
 * because the world is not perfect and my GDX only implementation stutters sometimes.
 * @author jorh
 */
public interface AudioInterface {
    public int create();
    public void dispose();
    
    public void setAPlay(AsyncPlay p);
    public AsyncPlay getAPlay();
    public boolean hasQuit();
    public boolean hasUnderflow();
    public short[] getBuffer();
}
