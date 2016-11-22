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
 * This class simulates JPSG line of chips which happen to be hypothetical.
 * I say simulate rather than emulate owing to use of an api rather than some binary transfer protocol.
 * @author jorh
 */
public class JPSG {
    
    public enum Models {
        JPSG1604M("JPSG1604M",false),
        JPSG1604S("JPSG1604S",true),
        JPSG1605M("JPSG1605M",false),
        JPSG1605S("JPSG1605S",true),
        JPSG1608M("JPSG1608M",false),
        JPSG1608S("JPSG1608S",true),
        JPSG1609M("JPSG1609M",false),
        JPSG1609S("JPSG1609S",true);
        
        public String name;
        public boolean stereo;
        
        private Models(String n,boolean s) {
            name = n;
            stereo = s;
        }
    }
    
    private Models Model; /**Model number of the chip
                                        // JPSG16XYZ
                                        // XY are how many channels. Type 1 comes in groups of 4. The remainder must be type 0.
                                        // Z is either 'S' for stereo or 'M' for mono sound.
                                        // Valid chips: 04M, 04S, 05M, 05S, 08M, 08S, 09M, 09S
                                        */
    private ChannelType0[] Channels0; // 8-bit PCM Bit Bang
    private ChannelType1[] Channels1; // 4-bit waveform tone and 4- or 1-bit noise generator
    private byte[] Stereo0;
    private byte[] Stereo1;
    
    public JPSG() {
        this(Models.JPSG1609S);
    }
    
    public JPSG(Models model) {
        Model = model;
        switch (Model) {
            case JPSG1609S:
            case JPSG1609M:
                //set up 1 Channels0
                Channels0 = new ChannelType0[1];
            case JPSG1608S:
            case JPSG1608M:
                //set up 8 Channels1
                Channels1 = new ChannelType1[8];
                break;
            case JPSG1605S:
            case JPSG1605M:
                //set up 1 Channels0
                Channels0 = new ChannelType0[1];
            case JPSG1604S:
            case JPSG1604M:
                //set up 4 Channels1
                Channels1 = new ChannelType1[4];
                break;
            default:
        }
        if (Channels0 != null && Channels0.length > 0) {
            Stereo0 = new byte[Channels0.length];
            for (int i=0; i<Channels0.length; ++i) Channels0[i] = new ChannelType0();
        }
        if (Channels1 != null && Channels1.length > 0) {
            Stereo1 = new byte[Channels1.length];
            for (int i=0; i<Channels1.length; ++i) Channels1[i] = new ChannelType1();
        }
    }
    
    //writing
        /**chip's stereo bytes
         @param i select channel type (0 or 1)
         @param s [n of channel type] __LR 0-3 or -1 for no change
         */
            public void setStereo(int i, int[] s) {
                if (s == null) return;
//                byte[] arr;
                switch (i) {
                    case 0:
                        for (int it=0; it<s.length && it<Stereo0.length; ++it) {
                            if (s[it] >= 0) Stereo0[it] = (byte)(s[it]&3);
                        }
                        break;
                    case 1:
                        for (int it=0; it<s.length && it<Stereo1.length; ++it) {
                            if (s[it] >= 0) Stereo1[it] = (byte)(s[it]&3);
                        }
                        break;
                    default:
                        //not recognized
//                        return;
                }
            }
            
        /**chip's channel1 noise bytes
         @param nz [ 0 to Channels1.length ] - 0 or 1 to set or -1 to not change
         */
            public void setNoise(int[] nz) {
                if (nz == null) return;
                for (int i=0; i<Channels1.length; ++i) 
                    Channels1[i].setNoise(nz[i] != 0);
            }
            
        /**channel1 sample data
         @param i select which channel1
         @param sam [n] - write first n samples each -8 to 7
         */
            public void setSamples(int i, byte[] sam) {
//                System.err.println(Arrays.toString(sam));
                if (sam == null || i < 0 || i >= Channels1.length) return;
                Channels1[i].setSamples(sam);
            }
            
        /**channel1 width
         @param i select which channel1
         @param w pulse width 0x0-F
         */
            public void setWidth(int i, int w) {
                if (i < 0 || i >= Channels1.length) return;
                Channels1[i].setWidth(w);
            }
            
        /**channel0 counter
         @param i select which channel1
         @param c set counter 0x000000-FFFFFF
         */
            public void setCounter0(int i, int c) {
                if (i < 0 || i >= Channels0.length) return;
                Channels0[i].setCounter(c);
            }
            
        /**channel0 stepper
         @param i select which channel1
         @param s set stepper 0x000000-FFFFFF (controls frequency)
         */
            public void setStepper0(int i, int s) {
//                System.err.printf("%d , %d, %d\n", i , s, Channels0.length);
                if (i < 0 || i >= Channels0.length) return;
//                System.err.println("setStepper");
                Channels0[i].setStepper(s);
//                System.err.println("Channels0[i].Stepper = s;");
            }
            
        /**channel1 counter
         @param i select which channel1
         @param c set counter 0x000000-FFFFFF
         */
            public void setCounter1(int i, int c) {
                if (i < 0 || i >= Channels1.length) return;
                Channels1[i].setCounter(c);
            }
            
        /**channel1 stepper
         @param i select which channel1
         @param s set stepper 0x000000-FFFFFF (controls frequency)
         */
            public void setStepper1(int i, int s) {
//                System.err.printf("%d , %d, %d\n", i , s, Channels1.length);
                if (i < 0 || i >= Channels1.length) return;
//                System.err.println("setStepper");
                Channels1[i].setStepper(s);
//                System.err.println("Channels1[i].Stepper = s;");
            }
            
        /**channel1 number of samples
         @param i select which channel1
         @param n number of samples used in the tone 0-64
         */
            public void setnSamples(int i, int n) {
                if (i < 0 || i >= Channels1.length) return;
                Channels1[i].setnSamples(n);
            }
            
        /**channel0 volume
         @param i select which channel0
         @param v volume values 0-7 (ghetto volume)
         */
            public void setVolume(int i, int v) {
                if (i < 0 || i >= Channels0.length) return;
                Channels0[i].setVolume(v);
            }
            
        /**channel0 loop
         @param i select which channel0
         @param l loop start values 0-FF
         */
            public void setLoop(int i, short l) {
                if (i < 0 || i >= Channels0.length) return;
                Channels0[i].setLoop(l);
            }
            
        /**channel0 end
         @param i select which channel0
         @param e loop end values 0-FF
         */
            public void setEnd(int i, int e) {
                if (i < 0 || i >= Channels0.length) return;
                Channels0[i].setEnd(e);
            }
            
        /**channel0 sample
         @param i select which channel0
         @param buf up to 256 byte of greatness
         */
            public void setBuffer(int i, byte[] buf) {
                if (i < 0 || i >= Channels0.length) return;
                Channels0[i].setBuffer(buf);
            }
            
        /**increment counters*/
            public void step() {
                for (int i=0; Channels0!=null && i<Channels0.length; ++i) {
                    Channels0[i].step();
                }
                for (int i=0; Channels1!=null && i<Channels1.length; ++i) {
                    Channels1[i].step();
                }
            }

    //reading
        /**get chip model
         @return chip's model
         */
            public Models getModel() {
                return Model;
            }
            
        /**get chip model
         @return chip's model name
         */
            public String getModelName() {
                return Model.name;
            }
            
        /**chip's stereo bytes
         @param i channel type (0 or 1)
         @return byte[n of channel type] 0-3 ____ __LR
         */
            public byte[] getStereo(int i) {
                switch (i) {
                    case 0:
                        return Stereo0.clone();
                    case 1:
                        return Stereo1.clone();
                    default:
                        return null;
                }
            }
            
        /**chip's channel1 noise bytes
         @return byte[Channels1.length] (0 or 1)
         */
            public byte[] getNoise() {
                if (Channels1.length <= 0) return null;
                byte[] arr = new byte[Channels1.length];
                for (int it=0; it<arr.length; ++it) {
                    if (Channels1[it].getNoise()) arr[it] = 1;
                    else arr[it] = 0;
                }
                return arr;
            }
            
        /**channel1 sample data
         @param i select which channel1
         @return byte[64] -8 to 7
         */
            public byte[] getSamples(int i) {
                if (i < 0 || i >= Channels1.length) return null;
                return Channels1[i].getSamples(); //already a clone here
            }
            
        /**channel1 width
         @param i select which channel1
         @return 0x0-F
         */
            public byte getWidth(int i) {
                if (i < 0 || i >= Channels1.length) return 0;
                return Channels1[i].getWidth();
            }
            
        /**channel0 counter
         @param i select which channel1
         @return 0x000000-FFFFFF
         */
            public int getCounter0(int i) {
                if (i < 0 || i >= Channels0.length) return 0;
                return Channels0[i].getCounter();
            }
            
        /**channel0 stepper
         @param i select which channel1
         @return 0x000000-FFFFFF
         */
            public int getStepper0(int i) {
                if (i < 0 || i >= Channels0.length) return 0;
                return Channels0[i].getStepper();
            }
            
        /**channel1 counter
         @param i select which channel1
         @return 0x000000-FFFFFF
         */
            public int getCounter1(int i) {
                if (i < 0 || i >= Channels1.length) return 0;
                return Channels1[i].getCounter();
            }
            
        /**channel1 stepper
         @param i select which channel1
         @return 0x000000-FFFFFF
         */
            public int getStepper1(int i) {
                if (i < 0 || i >= Channels1.length) return 0;
                return Channels1[i].getStepper();
            }
            
        /**channel1 number of samples
         @param i select which channel1
         @return 0-64
         */
            public byte getnSamples(int i) {
                if (i < 0 || i >= Channels1.length) return 0;
                return Channels1[i].getnSamples();
            }
            
        /**channel0 volume
         @param i select which channel0
         @return 0-7 (ghetto volume)
         */
            public byte getVolume(int i) {
                if (i < 0 || i >= Channels0.length) return 0;
                return Channels0[i].getVolume();
            }
            
        /**channel0 volume
         @param i select which channel0
         @return 0-FF loop begin
         */
            public short getLoop(int i) {
                if (i < 0 || i >= Channels0.length) return 0;
                return Channels0[i].getLoop();
            }
            
        /**channel0 volume
         @param i select which channel0
         @return 0-FF loop end
         */
            public short getEnd(int i) {
                if (i < 0 || i >= Channels0.length) return 0;
                return Channels0[i].getEnd();
            }
            
        /**channel0 buffer
         @param i select which channel0
         @return -128 to 127
         */
            public byte[] getBuffer(int i) {
                if (i < 0 || i >= Channels0.length) return null;
                return Channels0[i].getBuffer();
            }
            
        /**calculate sample data
         @return short[2] left sample then right sample
         */
            public short[] output() {
                
                if (Model == null) return null;
                short l0 = 0, r0 = 0, l1 = 0, r1 = 0;
                short[] ret = new short[]{0,0};
                byte b = 0;
                for (int it=0; Channels0!=null && it<Channels0.length; ++it) { //collect all type 0 samples
                    b = Channels0[it].output();
                    if ((!Model.stereo && Stereo0[it] != 0) || (Stereo0[it]&2) != 0) l0 += b;
                    if (Model.stereo && (Stereo0[it]&1) != 0) r0 += b;
                }
                for (int it=0; Channels1!=null && it<Channels1.length; ++it) { //collect all type 1 samples
                    b = Channels1[it].output();
                    if ((!Model.stereo && Stereo1[it] != 0) || (Stereo1[it]&2) != 0) l1 += b;
                    if (Model.stereo && (Stereo1[it]&1) != 0) r1 += b;
                }
                
                switch (Model) { //mix samples based on which chip this is
                case JPSG1609S:
                case JPSG1609M:
                        l0 = (short)(l0*(1<<7)); // 8 bit to 15 bit
                        l1 = (short)(l1*(1<<8)); // 7 bit to 15 bit
                        if (Model.stereo) {
                            r0 = (short)(r0*(1<<7));
                            r1 = (short)(r1*(1<<8));
                            ret[0] = (short)(l0+l1); //15 bit + 15 bit = 16 bit
                            ret[1] = (short)(r0+r1);
                        }
                        else {
                            ret[0] = ret[1] = (short)(l0+l1);
                        }
                        break;
                case JPSG1608S:
                case JPSG1608M:
                        l1 = (short)(l1*(1<<9)); //7 bit to 16 bit
                        if (Model.stereo) {
                            r1 = (short)(r1*(1<<9));
                            ret[0] = l1;
                            ret[1] = r1;
                        }
                        else {
                            ret[0] = ret[1] = l1;
                        }
                        break;
                case JPSG1605S:
                case JPSG1605M:
                        l0 = (short)(l0*(1<<7)); //8 bit to 15 bit
                        l1 = (short)(l1*(1<<9)); //6 bit to 15 bit
                        if (Model.stereo) {
                            r0 = (short)(r0*(1<<7));
                            r1 = (short)(r1*(1<<9));
                            ret[0] = (short)(l0+l1); //15 bit + 15 bit = 16 bit
                            ret[1] = (short)(r0+r1);
                        }
                        else {
                            ret[0] = ret[1] = (short)(l0+l1);
                        }
                        break;
                case JPSG1604S:
                case JPSG1604M:
                        l1 = (short)(l1*(1<<10)); //6 bit 16 bit
                        if (Model.stereo) {
                            r1 = (short)(r1*(1<<10));
                            ret[0] = l1;
                            ret[1] = r1;
                        }
                        else {
                            ret[0] = ret[1] = l1;
                        }
                        break;
                    default:
                        return null;
                }
                return ret;
            }
            
}
