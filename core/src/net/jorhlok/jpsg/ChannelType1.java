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
 * Channel Type 1 "4-bit waveform tone and 4 or 1 bit noise generator." 
 * 
 * Uses up to 64 4-bit samples as a waveform. 
 * Uses 24-bit index for the waveform.
 * Has Pulse
 * Width able to be applied to any waveform. 
 * Volume is a function of the waveform and is taken care of by the play routine. 
 * Noise generator. 
 *      Width used as volume;
 *
 * @author jorh
 */
public class ChannelType1 {
    
    static private long HashTable[][];

    static private int PWMTable[][]; // how long a sample takes = PWMTable[sample size-1][pulse width*2+side]; side = 0-front or 1-back
    static private final int MaxCount = (1 << 24) - 1; //24 bit counter 0 to 2^24-1
//24 bit counter 0 to 2^24-1

    private byte Width = 0; // 0x0-F squeezes left from 16/32 to 1/32 (Width+1)/32
    private byte[] Samples; 
    private byte nSamples = 0; //1 to 64 for active sound
    private boolean Noise = false;

    private int Counter = 0; // 24-bit index to the waveform
    private int Stepper = 0; // 24-bit addition to the counter each sample

    public ChannelType1() {
        byte[] garbage = new byte[64];
        for (int i = 0; i < 64; ++i) { //because why not?
            garbage[i] = (byte) Math.round(Math.random() * 16 - 8);
        }
        init(0, garbage, 0, false);
    }

    public ChannelType1(int w, byte[] sam, int n, boolean nz) {
        init(w, sam, n, nz);
    }

    private void init(int w, byte[] sam, int n, boolean nz) {
        this.Samples = new byte[64];
        setWidth(w);
        setSamples(sam);
        initPWMTable();
        initHashTable();
        setnSamples(n);
        Noise = nz;
    }

    static private void initPWMTable() {
        if (PWMTable == null) {
            PWMTable = new int[64][32];
            for (int sl = 1; sl <= 64; sl++) {
                for (int pw = 16; pw > 0; pw--) {
                    PWMTable[sl - 1][pw * 2 - 2] = (int) (MaxCount * (pw / (sl * 16f))); //peak sample lengths
                    PWMTable[sl - 1][pw * 2 - 1] = (int) ((MaxCount - (PWMTable[sl - 1][pw * 2 - 2] * Math.floor(sl / 2f))) / Math.ceil(sl / 2f));
                }
            }
        }
    }
    
    static private void initHashTable() {
        if (HashTable == null) {
            HashTable = new long[16][16];
            for (int i=0; i<16; ++i) {
                long exp = (long)Math.pow(63, 15-i);
                for (int j=0; j<16; ++j) {
                    HashTable[i][j] = (j+1)*exp;
                }
            }
        }
    }

    static private byte getSampleIndex(byte size, byte width, int counter) {
        byte i = 0;
        if (size < 1 || size > 64 || width < 0 || width > 15 || counter < 0 || counter > MaxCount) {
            return 0;
        }
        int front = PWMTable[size - 1][width << 1] * (size >> 1);
        if (counter < front) {
            i = (byte) (counter / PWMTable[size - 1][width << 1]);
        } else {
            i = (byte) (((counter - front) / PWMTable[size - 1][(width << 1) + 1]) + (size >> 1));
        }
        return i;
    }

    public void setWidth(int w) {
        if (w < 0) Width = 0;
        else Width = (byte) (w & 15);
    }

    public byte getWidth() {
        return Width;
    }

    public byte[] getSamples() {
        return Samples.clone();
    }

    public void setSamples(byte[] sam) {
        for (byte i = 0; i < sam.length && i < 64; ++i) {
            if (sam[i] > 0) {
                Samples[i] = (byte) Math.min(sam[i], 7);
            } else {
                Samples[i] = (byte) Math.max(sam[i], -8);
            }
        }
    }

    public byte getnSamples() {
        return nSamples;
    }

    public void setnSamples(int n) {
        if (n < 0) {
            nSamples = 0;
        } else if (n > 64) {
            nSamples = 64;
        } else {
            nSamples = (byte) n;
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

    public boolean getNoise() {
        return Noise;
    }

    public void setNoise(boolean Noise) {
        this.Noise = Noise;
    }
    
    /**
     * Calculates output.
     * @return Sound output from -8 to 7
     */
    public byte output() {
        if ( (nSamples <= 0 || nSamples > 64) && !Noise ) {
            return 0;
        }
        byte sample = 0;
        if (Noise) {
            if (nSamples == 0) {
                sample = (byte)(Samples[Counter>>18]+8); //get nybble via high 6 bits of counter
                sample = (byte)( (sample>>( (Counter>>16)&7 ))&1 ); //get bit as addressed by lower 3 bits of the high byte of counter
                if (sample == 0) return (byte)( Width>>1 ); //increment on even numbers
                else return (byte)( -1*(Width>>1)-(Width&1) ); //increment on odd numbers
            }
            else {
                sample = Samples[Counter>>18];
                if (sample >= Width>>1) return (byte)( Width>>1 ); //increment on even numbers
                else if (sample <= -1*(Width>>1)-(Width&1)) return (byte)( -1*(Width>>1)-(Width&1) ); //increment on odd numbers
                return sample;
            }
        }
        else sample = Samples[getSampleIndex(nSamples, Width, Counter)]; //look up sample
        return sample;
    }

    public void step() {
        Counter += Stepper;
        if (Counter > MaxCount) {
            Counter &= MaxCount;
            if (Noise && nSamples != 0) {
                //64-bit hash and shift
                //2^256 different states
                //theoretically some type of cycle but I don't know how long that might be
                for (int i=0; i<Samples.length/16; ++i) {
                    long l = 0;
                    for (int j=0; j<16; ++j) {
                        l += HashTable[j][Samples[i*16+j] + 8];
                    }
                    for (int j=0; j<16; ++j) {
                        Samples[i*16+j] = (byte)( (l&15) - 8 );
                        l >>= 4;
                    }
                }
                byte b = Samples[0];
                for (int i=0; i<Samples.length-1; ++i) {
                    Samples[i] = Samples[i+1];
                }
                Samples[Samples.length-1] = b;
            }
        }
    }
}
