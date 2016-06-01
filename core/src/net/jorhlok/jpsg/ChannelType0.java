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
 * Channel Type 0 "8-bit PCM Bit Bang"
 * 
 * Waveform modulation done by the play routine.
 * Includes ghetto 3-bit volume that happens to be logarithmic by bit shifting.
 * @author jorh
 */
public class ChannelType0 {
    private byte Volume = 0; //0-7 (ghetto volume control)
    private byte Sample = 0; //The stored sample

    public byte getVolume() {
        return Volume;
    }

    public void setVolume(int v) {
        Volume = (byte)(v&7);
    }

    public byte getSample() {
        return Sample;
    }

    public void setSample(int s) {
        Sample = (byte)s;
    }
    
    public byte output() {
        if (Sample >= 0) return (byte)( Sample>>(7-Volume) );
        else return (byte)( -1*( (-1*Sample)>>(7-Volume) ) );
    }
}