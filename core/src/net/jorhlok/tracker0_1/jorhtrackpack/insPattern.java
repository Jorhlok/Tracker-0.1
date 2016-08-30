package net.jorhlok.tracker0_1.jorhtrackpack;

/**
 * Pattern of standard instruments
 * @author jorh
 */
public class insPattern {

    public short Length; // 0-FF
    public String[] Note;
    public byte[] Stereo; // 0, 1, 2, or 3 for mute, left, right, both (-1 for null)
    public byte[] Volume; // 0-F (-1 for null)
    public byte[] Width; // 0-F (-1 for null)
    public short[] Instrument; // 0-FF (-1 for null)
    public char[] Effect; // Alphanumeric + Punctuation 20-7E
    public byte[] FX1; // 0-F (-1 for null)
    public byte[] FX2; // 0-F (-1 for null)

    public insPattern() {
        Length = 0;
        Note = new String[256];
        Stereo = new byte[256];
        Volume = new byte[256];
        Width = new byte[256];
        Instrument = new short[256];
        Effect = new char[256];
        FX1 = new byte[256];
        FX2 = new byte[256];
        for (short i = 0; i < 256; ++i) {
            Stereo[i] = -1;
            Volume[i] = -1;
            Width[i] = -1;
            Instrument[i] = -1;
            Effect[i] = ' ';
            FX1[i] = -1;
            FX2[i] = -1;
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
            ret += Integer.toHexString(Stereo[index] & 3).toUpperCase();
        } else {
            ret += ",";
        }

        if (Volume[index] >= 0) {
            ret += Integer.toHexString(Volume[index] & 15).toUpperCase();
        } else {
            ret += ",";
        }

        if (Width[index] >= 0) {
            ret += Integer.toHexString(Width[index] & 15).toUpperCase();
        } else {
            ret += ",";
        }

        if (Instrument[index] >= 0) {
            str = Integer.toHexString(Instrument[index]).toUpperCase();
            switch (2 - str.length()) {
                case 1:
                    ret += "0" + str;
                    break;
                case 0:
                    ret += str;
                    break;
                default:
                    ret += str.substring(str.length() - 2);
            }
        } else {
            ret += "..";
        }

        if (Effect[index] >= (char) 32 && Effect[index] <= (char) 126) {
            ret += Effect[index];
        } else {
            ret += " "; // 32 or space
        }

        if (FX1[index] >= 0) {
            str = Integer.toHexString(FX1[index] & 15).toUpperCase();
            switch (str.length()) {
                case 1:
                    ret += str;
                    break;
                default:
                    ret += str.substring(str.length() - 1);
            }
        } else {
            ret += "/";
        }

        if (FX2[index] >= 0) {
            str = Integer.toHexString(FX2[index] & 15).toUpperCase();
            switch (str.length()) {
                case 1:
                    ret += str;
                    break;
                default:
                    ret += str.substring(str.length() - 1);
            }
        } else {
            ret += "/";
        }

        return ret;
    }

    @Override
    public String toString() {
        return null;
    }
}
