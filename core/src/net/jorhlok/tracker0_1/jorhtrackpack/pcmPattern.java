package net.jorhlok.tracker0_1.jorhtrackpack;

/**
 * Pattern of PCM8 commands
 * @author jorh
 */
public class pcmPattern {
    public short Length; //0-FF (-1 for null)
    public String[] Note;
    public byte[] Stereo; // 0, 1, 2, or 3 for mute, left, right, both (-1 for null)
    public byte[] Volume; //0-3F (63) (-1 for null)
    public short[] Waveform; //0-FF (-1 for null)
    
    public pcmPattern() {
        Length = 0;
        Note = new String[256];
        Stereo = new byte[256];
        Volume = new byte[256];
        Waveform = new short[256];
        for (short i=0; i<256; ++i) {
            Stereo[i] = -1;
            Volume[i] = -1;
            Waveform[i] = -1; 
        }
    }
    
    public String noteToString(short index) {
        String ret;
        String str;
        
        str = Note[index];
        if (str == null) {
            ret = "::::::";
        } else {
            switch (6 - str.length()) {
                case 5:
                    ret = str + ":::::";
                    break;
                case 4:
                    ret = str + "::::";
                    break;
                case 3:
                    ret = str + ":::";
                    break;
                case 2:
                    ret = str + "::";
                    break;
                case 1:
                    ret = str + ":";
                    break;
                case 0:
                    ret = str;
                    break;
                default: // length > 6
                    ret = str.substring(0, 6);
            }
            ret = ret.replace(' ', ':');
        }
        
        if (Stereo[index] >= 0) {
            ret += Integer.toHexString(Stereo[index] & 3);
        } else {
            ret += ",";
        }
        
        if (Volume[index] >= 0) {
            ret += Integer.toHexString(Volume[index]&7);
        }
        else ret += ",";
        
        
        if (Waveform[index] >= 0) {
            str = Integer.toHexString(Waveform[index]);
            switch (2-str.length()) {
                case 1:
                    ret += "0"+str;
                    break;
                case 0:
                    ret += str;
                    break;
                default:
                    ret += str.substring(str.length()-2);
            }
        }
        else ret += "..";
        
        
        return ret;
    }
    
    @Override
    public String toString() {
        return null;
    }
}