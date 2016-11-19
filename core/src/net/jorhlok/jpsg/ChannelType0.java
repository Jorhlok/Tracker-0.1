/*
 * Package net.jorhlok.jpsg is copyright 2016 to alias Jorhlok.
 * Licensed to anybody in the universe under the zlib license as shown below.
    
    http://www.zlib.net/zlib_license.html

  This software is provided 'as-is', without any express or implied
  warranty.  In no event will the authors be held liable for any damages
  arising from the use of this software.

  Permission is granted to anyone to use this software for any purpose,
  including commercial applications, and to alter it and redistribute it
  freely, subject to the following restrictions:

  1. The origin of this software must not be misrepresented; you must not
     claim that you wrote the original software. If you use this software
     in a product, an acknowledgment in the product documentation would be
     appreciated but is not required.
  2. Altered source versions must be plainly marked as such, and must not be
     misrepresented as being the original software.
  3. This notice may not be removed or altered from any source distribution.

 */
package net.jorhlok.jpsg;

/**
 * Channel Type 0 "8-bit PCM"
 * 
 * Waveform modulation done from a buffer of X bytes.
 * Includes ghetto 3-bit volume that happens to be logarithmic by bit shifting.
 * @author jorh
 */
public class ChannelType0 {
    static private final int MaxCount = (1 << 24) - 1; //24 bit counter 0 to 2^24-1
    private byte[] Buffer = new byte[256];
    private byte Volume = 0; //0-7 (ghetto volume control)
    private short Loop = 0;
    private short End = 255;
    private int Counter = 0;
    private int Stepper = 0;

    public byte getVolume() {
        return Volume;
    }

    public void setVolume(int v) {
        Volume = (byte)(v&7);
    }

    public short getLoop() {
        return Loop;
    }

    public void setLoop(int l) {
        Loop = (short)(l&255);
    }

    public short getEnd() {
        return End;
    }

    public void setEnd(int e) {
        End = (short)(e&255);
    }
    
    public byte[] getBuffer() {
        return Buffer.clone();
    }

    public void setBuffer(byte[] sam) {
        for (byte i = 0; i < sam.length && i < Buffer.length; ++i) {
            if (sam[i] > 0) {
                Buffer[i] = (byte) Math.min(sam[i], 127);
            } else {
                Buffer[i] = (byte) Math.max(sam[i], -128);
            }
        }
    }
    
    public int getCounter() {
        return Counter;
    }

    public void setCounter(int c) {
        if (c < 0) {
            Counter = 0;
        } else if (c > MaxCount) {
            Counter = MaxCount;
        } else {
            Counter = c;
        }
    }

    public int getStepper() {
        return Stepper;
    }

    public void setStepper(int s) {
        if (s < 0) {
            Stepper = 0;
        } else if (s > MaxCount) {
            Stepper = MaxCount;
        } else {
            Stepper = s;
        }
    }
    
    public byte output() {
        byte sample = Buffer[Counter/(MaxCount+1)*Buffer.length];
        if (sample >= 0) return (byte)( sample>>(7-Volume) );
        else return (byte)( -1*( (-1*sample)>>(7-Volume) ) ); //might be unnecessary if java's bit shift is arithmetic instead of logical but this doesn't hurt it I guess
    }
    
    public void step() {
        Counter += Stepper;
        if (Counter > MaxCount) Counter = Loop<<16;
    }
}
